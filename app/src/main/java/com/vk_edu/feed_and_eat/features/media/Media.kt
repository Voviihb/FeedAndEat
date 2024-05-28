package com.vk_edu.feed_and_eat.features.media

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.app.Activity
import android.os.Build
import androidx.core.app.ActivityCompat

object Media {
    fun requestMediaPermission(activity: Activity) {
        val permissionArray =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(READ_MEDIA_IMAGES)
            } else {
                arrayOf(READ_EXTERNAL_STORAGE)
            }
        ActivityCompat.requestPermissions(
            activity,
            permissionArray,
            MEDIA_REQUEST_CODE
        )
    }

    private const val MEDIA_REQUEST_CODE = 26
}