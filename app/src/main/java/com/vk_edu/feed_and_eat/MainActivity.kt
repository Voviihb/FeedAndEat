package com.vk_edu.feed_and_eat

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.vk_edu.feed_and_eat.features.cooking.domain.TimerService
import com.vk_edu.feed_and_eat.features.cooking.pres.CookingScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

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

    private fun startTimer(timerId: String, time: Int) {
        val timerService = Intent(this, TimerService::class.java)
        timerService.putExtra(
            TimerService.ACTION,
            TimerService.ACTION_START
        )
        timerService.putExtra(
            TimerService.TIMER_ID,
            timerId
        )
        timerService.putExtra(
            TimerService.TIMER_TIME,
            time
        )
        startService(timerService)
    }

    private fun stopTimer(timerId: String) {
        val timerService = Intent(this, TimerService::class.java)
        timerService.putExtra(
            TimerService.ACTION,
            TimerService.ACTION_STOP
        )
        timerService.putExtra(
            TimerService.TIMER_ID,
            timerId
        )
        startService(timerService)
    }
}