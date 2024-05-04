package com.vk_edu.feed_and_eat.features.recipe.domain

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.vk_edu.feed_and_eat.R
import com.vk_edu.feed_and_eat.features.notifications.Notifications
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

data class TimerState(
    val remainingSec : Int,
    val totalSec : Int,
    val isPaused: Boolean,
)

class TimerService : Service() {
    private val timerJobs = mutableMapOf<String, Job>()
    private val timerValues = mutableMapOf<String, TimerState>()
    private var isStopWatchRunning = false
    private var updateTimer: Timer? = null

    private val _activeTimerUpdates = MutableStateFlow<Map<String, TimerState>>(emptyMap())
    val activeTimerUpdates: StateFlow<Map<String, TimerState>> = _activeTimerUpdates

    inner class LocalBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    private val binder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        if (intent != null) {
            val action = intent.getStringExtra(ACTION)
            val timerId = intent.getStringExtra(TIMER_ID)
            val time = intent.getIntExtra(TIMER_TIME, 0)
            when (action) {
                ACTION_START -> startTimer(timerId, time, time)
                ACTION_STOP -> stopTimer(timerId)
                ACTION_PAUSE -> pauseTimer(timerId)
                ACTION_RESUME -> resumeTimer(timerId)
                ACTION_CANCEL_ALL -> cancelAllTimers()
            }
        }
        return START_NOT_STICKY
    }

    private fun moveToForeground() {
        if (!isStopWatchRunning) {
            updateTimer = Timer()
            updateTimer?.schedule(object : TimerTask() {
                override fun run() {
                    _activeTimerUpdates.tryEmit(timerValues.toMap())
                    updateNotification()
                }
            }, 0, 1000)
        }
        val notification = createNotification()
        ServiceCompat.startForeground(this, 1, notification, 0)
    }

    private fun startTimer(
        timerId: String?,
        remainingSec: Int,
        totalSec: Int,
    ) {
        val timerClass = TimerState(remainingSec, totalSec, false)
        if (timerId != null) {
            val job = CoroutineScope(Dispatchers.IO).launch {
                var secondsLeft = remainingSec
                timerValues[timerId] = timerClass
                while (secondsLeft > 0) {
                    delay(1000)
                    secondsLeft--
                    timerValues[timerId] = timerValues[timerId]?.copy(remainingSec = secondsLeft) ?: TimerState(secondsLeft, totalSec, true)
                }
                sendOnFinishNotification(timerId)
                stopTimer(timerId)
            }
            timerJobs[timerId] = job
            moveToForeground()
            isStopWatchRunning = true
        }
    }

    private fun stopTimer(timerId: String?) {
        timerJobs[timerId]?.cancel()
        timerJobs.remove(timerId)
        timerValues.remove(timerId)
        updateNotification()
        if (timerJobs.isEmpty()) {
            updateTimer?.cancel()
            isStopWatchRunning = false
            _activeTimerUpdates.tryEmit(timerValues.toMap())
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
    }

    private fun pauseTimer(timerId: String?) {
        if (timerId != null) {
            timerJobs[timerId]?.cancel()
            timerJobs.remove(timerId)
            timerValues[timerId] = timerValues[timerId]!!.copy(isPaused = true)
            updateNotification()
        }
    }

    private fun resumeTimer(
        timerId: String?,
    ) {
        if (timerId != null) {
            timerValues[timerId] = timerValues[timerId]!!.copy(isPaused = false)
            val timerLeft = timerValues[timerId]
            if (timerLeft != null && timerLeft.remainingSec >= 0) {
                startTimer(timerId, timerLeft.remainingSec, timerLeft.totalSec)
            }
        }
    }

    private fun cancelAllTimers() {
        timerJobs.forEach { (_, job) ->
            job.cancel()
        }
        timerJobs.clear()
        timerValues.clear()
        updateTimer?.cancel()
        isStopWatchRunning = false
        _activeTimerUpdates.tryEmit(timerValues.toMap())
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun createNotification(): Notification {
        val pendingIntent = Notifications.getPendingIntentForTimer(
            this,
            CHANNEL_ID_TIMER,
            CHANNEL_NAME_TIMER,
            NotificationManager.IMPORTANCE_LOW
        )

        val cancelAllIntent = Intent(this, TimerService::class.java)
        cancelAllIntent.putExtra(
            ACTION,
            ACTION_CANCEL_ALL
        )
        val pendingCancelAllIntent = PendingIntent.getService(
            this,
            CANCEL_INTENT_REQUEST_CODE,
            cancelAllIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID_TIMER)
            .setContentTitle(FEED_AND_EAT_TIMER)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentText(getString(R.string.running_timers, timerJobs.keys.size.toString()))
            .setSmallIcon(R.drawable.app_logo)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.cooked_icon,
                getString(R.string.cancel_all), pendingCancelAllIntent
            )
            .setColor(ContextCompat.getColor(this, R.color.light_purple_fae))

        var style = NotificationCompat.InboxStyle()
        timerJobs.forEach { (timerId, _) ->
            val time = timerValues[timerId]?.remainingSec?.toLong() ?: 0
            val hours = TimeUnit.SECONDS.toHours(time)
            val minutes = TimeUnit.SECONDS.toMinutes(time) % 60
            val seconds = TimeUnit.SECONDS.toSeconds(time) % 60 % 60
            style =
                style.addLine(
                    getString(R.string.timer_is_at).format(
                        timerId,
                        hours,
                        minutes,
                        seconds
                    )
                )
        }
        timerValues.filter { it.value.isPaused }.forEach { (timerId, _) ->
            val time = timerValues[timerId]?.remainingSec?.toLong() ?: 0
            val hours = TimeUnit.SECONDS.toHours(time)
            val minutes = TimeUnit.SECONDS.toMinutes(time) % 60
            val seconds = TimeUnit.SECONDS.toSeconds(time) % 60 % 60
            style = style.addLine(
                getString(R.string.paused_timer_is_at).format(
                    timerId,
                    hours,
                    minutes,
                    seconds
                )
            )
        }
        return notification.setStyle(style).build()
    }

    private fun updateNotification() {
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(
            TIMER_NOTIFICATION_ID,
            createNotification()
        )
    }

    private fun sendOnFinishNotification(timerId: String?) {
        val pendingIntent = Notifications.getPendingIntentForTimer(
            this,
            CHANNEL_ID_ON_DONE,
            CHANNEL_NAME_ON_DONE,
            NotificationManager.IMPORTANCE_HIGH
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID_ON_DONE)
            .setContentTitle(FEED_AND_EAT_TIMER)
            .setAutoCancel(true)
            .setContentText(getString(R.string.timer_is_done, timerId))
            .setSmallIcon(R.drawable.app_logo)
            .setContentIntent(pendingIntent)
            .setColor(ContextCompat.getColor(this, R.color.light_purple_fae))
            .build()

        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(timerId, ON_DONE_NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        timerJobs.forEach { (_, job) ->
            job.cancel()
        }
        timerJobs.clear()
        timerValues.clear()
        updateTimer = null
    }

    companion object {
        private const val CHANNEL_ID_TIMER = "TimerServiceChannel"
        private const val CHANNEL_ID_ON_DONE = "Timer OnDone"
        private const val CHANNEL_NAME_TIMER = "Timer Service Channel"
        private const val CHANNEL_NAME_ON_DONE = "Timer OnDone Notifications"
        private const val FEED_AND_EAT_TIMER = "Feed&Eat timer"

        const val ACTIVITY_INTENT_REQUEST_CODE = 0
        private const val CANCEL_INTENT_REQUEST_CODE = 1
        private const val TIMER_NOTIFICATION_ID = 1
        private const val ON_DONE_NOTIFICATION_ID = 2

        const val ACTION = "action"
        const val TIMER_ID = "timerId"
        const val TIMER_TIME = "time"
        const val ACTION_START = "start"
        const val ACTION_STOP = "stop"
        const val ACTION_PAUSE = "pause"
        const val ACTION_RESUME = "resume"
        const val ACTION_CANCEL_ALL = "cancel_all"
    }
}