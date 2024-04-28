package com.vk_edu.feed_and_eat.features.cooking.domain

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.vk_edu.feed_and_eat.MainActivity
import com.vk_edu.feed_and_eat.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask


class TimerService : Service() {
    private val timerJobs = mutableMapOf<String, Job>()
    private val timerValues = mutableMapOf<String, Int>()
    private val pausedTimers = mutableMapOf<String, Int>()
    private var isStopWatchRunning = false
    private var updateTimer: Timer? = null

    private val _activeTimerUpdates = MutableSharedFlow<Map<String, Int>>(replay = 1)
    val activeTimerUpdates: SharedFlow<Map<String, Int>> = _activeTimerUpdates

    private val _pausedTimerUpdates = MutableSharedFlow<Map<String, Int>>(replay = 1)
    val pausedTimerUpdates: SharedFlow<Map<String, Int>> = _pausedTimerUpdates


    inner class LocalBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    private val binder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.getStringExtra(ACTION)
            val timerId = intent.getStringExtra(TIMER_ID)
            val time = intent.getIntExtra(TIMER_TIME, 0)
            when (action) {
                ACTION_START -> startTimer(timerId, time)
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
            updateTimer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    _activeTimerUpdates.tryEmit(timerValues.toMap())
                    _pausedTimerUpdates.tryEmit(pausedTimers.toMap())
                    updateNotification()
                }
            }, 0, 1000)
        }
        val notification = createNotification()
        ServiceCompat.startForeground(this, 1, notification, 0)
    }

    private fun startTimer(timerId: String?, time: Int) {
        if (timerId != null) {
            val job = CoroutineScope(Dispatchers.IO).launch {
                var secondsLeft = time
                while (secondsLeft > 0) {
                    delay(1000)
                    secondsLeft--
                    timerValues[timerId] = secondsLeft
                }
            }
            timerJobs[timerId] = job
            timerValues[timerId] = time
            moveToForeground()
            isStopWatchRunning = true
        }
    }

    private fun stopTimer(timerId: String?) {
        timerJobs[timerId]?.cancel()
        timerJobs.remove(timerId)
        timerValues.remove(timerId)
        updateNotification()
        if (timerJobs.isEmpty() && pausedTimers.isEmpty()) {
            updateTimer?.cancel()
            isStopWatchRunning = false
            _activeTimerUpdates.tryEmit(timerValues.toMap())
            _pausedTimerUpdates.tryEmit(pausedTimers.toMap())
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
    }

    private fun pauseTimer(timerId: String?) {
        if (timerId != null) {
            timerJobs[timerId]?.cancel()
            pausedTimers[timerId] = timerValues[timerId] ?: 0
            timerJobs.remove(timerId)
            timerValues.remove(timerId)
            updateNotification()
        }
    }

    private fun resumeTimer(timerId: String?) {
        if (timerId != null) {
            val timeLeft = pausedTimers[timerId]
            if (timeLeft != null && timeLeft >= 0) {
                startTimer(timerId, timeLeft)
            }
            pausedTimers.remove(timerId)
        }
    }

    private fun cancelAllTimers() {
        timerJobs.forEach { (_, job) ->
            job.cancel()
        }
        timerJobs.clear()
        timerValues.clear()
        pausedTimers.clear()

        updateTimer?.cancel()
        isStopWatchRunning = false
        _activeTimerUpdates.tryEmit(timerValues.toMap())
        _pausedTimerUpdates.tryEmit(pausedTimers.toMap())
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_MUTABLE
        )

        val cancelAllIntent = Intent(this, TimerService::class.java)
        cancelAllIntent.putExtra(
            ACTION,
            ACTION_CANCEL_ALL
        )
        val pendingCancelAllIntent = PendingIntent.getService(
            this,
            1,
            cancelAllIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        Log.d("Taag", "create notification")
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(FEED_AND_EAT_TIMER)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentText("Running ${timerJobs.keys.size} timers")
            .setSmallIcon(R.drawable.logo_feed_and_eat)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.cooked_icon, "Cancel all", pendingCancelAllIntent)

        var style = NotificationCompat.InboxStyle()
        timerJobs.forEach { (timerId, _) ->
            style = style.addLine("$timerId is at ${timerValues[timerId]} seconds")
        }
        pausedTimers.forEach { (timerId, _) ->
            style = style.addLine("$timerId paused, ${pausedTimers[timerId]} seconds left")
        }
        return notification.setStyle(style).build()
    }

    private fun updateNotification() {
        Log.d("Taag", "update notification")
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(
            1,
            createNotification()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        timerJobs.forEach { (_, job) ->
            job.cancel()
        }
        timerJobs.clear()
        timerValues.clear()
        pausedTimers.clear()
        updateTimer = null
    }

    companion object {
        private const val CHANNEL_ID = "TimerServiceChannel"
        private const val CHANNEL_NAME = "Timer Service Channel"
        private const val FEED_AND_EAT_TIMER = "Feed&Eat timer"

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