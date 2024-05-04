package com.vk_edu.feed_and_eat.features.notifications

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.vk_edu.feed_and_eat.MainActivity
import com.vk_edu.feed_and_eat.features.recipe.domain.TimerService

object Notifications {
    fun requestPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            /* Current version doesn't require permission */
            return
        }

        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            /* Permission is already granted */
            return
        }

        requestPermissions(
            activity,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            NOTIFICATIONS_REQUEST_CODE
        )
    }

    fun getPendingIntentForTimer(
        context: Context,
        channelId: String,
        channelName: String,
        importance: Int
    ): PendingIntent {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                importance
            )
            val manager = getSystemService(context, NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
        val notificationIntent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
            context, TimerService.ACTIVITY_INTENT_REQUEST_CODE, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }


    private const val NOTIFICATIONS_REQUEST_CODE = 25

}
