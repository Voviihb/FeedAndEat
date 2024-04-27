package com.vk_edu.feed_and_eat.features.cooking.domain

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.vk_edu.feed_and_eat.MainActivity
import com.vk_edu.feed_and_eat.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

class TimerService : Service() {

    private val timerJobs = mutableMapOf<String, Job>()
    private val timerValues = mutableMapOf<String, Int>()
    private var isStopWatchRunning = false
    private var updateTimer: Timer? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val action = it.getStringExtra(ACTION)
            val timerId = it.getStringExtra(TIMER_ID)
            when (action) {
                ACTION_START -> startTimer(timerId)
                ACTION_STOP -> stopTimer(timerId)
            }
        }
        return START_STICKY
    }

    private fun moveToForeground() {
        Log.d("Taag", "Move to foreground")
        if (!isStopWatchRunning) {
            updateTimer = Timer()
            updateTimer?.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    updateNotification()
                }
            }, 0, 1000)
        }
        val notification = createNotification()
        ServiceCompat.startForeground(this, 1, notification, 0)
    }

    private fun startTimer(timerId: String?) {
        timerId?.let {
            val job = GlobalScope.launch {
                flow {
                    var secondsPassed = 0
                    while (true) {
                        delay(1000)
                        emit(secondsPassed++)
                        timerValues[timerId] = secondsPassed
                    }
                }.collect {
                    // Обновление UI или выполнение других действий при каждом тике таймера
                    Log.d("Taag", "Timer $timerId: $it seconds passed")
                }
            }
            timerJobs[timerId] = job
            timerValues[timerId] = 0
            moveToForeground()
            isStopWatchRunning = true
            Log.d("Taag", timerJobs.toString())
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
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
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
        Log.d("Taag", "create notification")
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Feed&Eat timer")
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentText("Running ${timerJobs.keys.size} timers")
            .setSmallIcon(R.drawable.logo_feed_and_eat)
            .setContentIntent(pendingIntent)

        var style = NotificationCompat.InboxStyle()
        timerJobs.forEach { (timerId, _) ->
            style = style.addLine("$timerId is at ${timerValues[timerId]} seconds")
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
        updateTimer = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val CHANNEL_ID = "TimerServiceChannel"
        private const val CHANNEL_NAME = "Timer Service Channel"

        const val ACTION = "action"
        const val TIMER_ID = "timerId"
        const val ACTION_START = "start"
        const val ACTION_STOP = "stop"
    }
}