package com.vk_edu.feed_and_eat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.vk_edu.feed_and_eat.features.media.Media
import com.vk_edu.feed_and_eat.features.navigation.data.NavGraph
import com.vk_edu.feed_and_eat.features.notifications.Notifications
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Notifications.requestPermission(this)
        Media.requestMediaPermission(this)
        setContent {
            val navController = rememberNavController()
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                NavGraph(navController = navController, context = LocalContext.current)
            }
        }

    }
}