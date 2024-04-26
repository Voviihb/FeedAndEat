package com.vk_edu.feed_and_eat

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vk_edu.feed_and_eat.features.cooking.domain.TimerService
import com.vk_edu.feed_and_eat.features.cooking.pres.CookingScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var timerService: TimerService
    private var isServiceBound = false
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimerService.LocalBinder
            timerService = binder.getService()
            isServiceBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isServiceBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        val serviceIntent = Intent(this, TimerService::class.java)
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isServiceBound) {
            unbindService(connection)
            isServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            val navController = rememberNavController()
//            Column(
//                verticalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier.fillMaxSize()
//            ){
//                NavGraph(navController = navController, context = LocalContext.current)
//            }
            CookingScreen(start = ::startTimer, stop = ::stopTimer)
        }
    }

    private fun startTimer(timerId: String) {
        if (isServiceBound) {
            timerService.startTimer(timerId)
        }
    }

    private fun stopTimer(timerId: String) {
        if (isServiceBound) {
            timerService.stopTimer(timerId)
        }
    }
}