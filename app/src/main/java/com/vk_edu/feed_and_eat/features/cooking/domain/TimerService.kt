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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class TimerService : Service() {

    private val timerJobs = mutableMapOf<String, Job>()
    private val CHANNEL_ID = "TimerServiceChannel"

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val action = it.getStringExtra("action")
            val timerId = it.getStringExtra("timerId")
            when (action) {
                "start" -> startTimer(timerId)
                "stop" -> stopTimer(timerId)
            }
        }
        return START_STICKY
    }

    fun startTimer(timerId: String?) {
        timerId?.let {
            val notification = createNotification()
//            startForeground(1, notification)
//            val foregroundType = when {
//                Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
//                else -> 0
//            }
            ServiceCompat.startForeground(this, 100, notification, 0)
            val job = GlobalScope.launch {
                flow {
                    var secondsPassed = 0
                    while (true) {
                        delay(1000)
                        emit(secondsPassed++)
                    }
                }.collect {
                    // Обновление UI или выполнение других действий при каждом тике таймера
                    Log.d("Taag", "Timer $timerId: $it seconds passed")
                }
            }
            timerJobs[timerId] = job
        }
    }

    fun stopTimer(timerId: String?) {
        timerJobs[timerId]?.cancel()
        timerJobs.remove(timerId)
        stopSelf()
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Timer Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
            PendingIntent.FLAG_MUTABLE)
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Timer Service")
            .setContentText("Running")
            .setSmallIcon(R.drawable.logo_feed_and_eat)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        timerJobs.forEach { (_, job) ->
            job.cancel()
        }
        timerJobs.clear()
    }
}