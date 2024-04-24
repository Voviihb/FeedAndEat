package com.vk_edu.feed_and_eat.features.recipe.domain.repository

import com.vk_edu.feed_and_eat.features.dishes.domain.models.Timer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TimerImpl(private val totalMillis: Long) : TimerInterface{
    private var timer : Flow<Long>? = null
    override fun startTimer(millis: Long){
        if (timer != null){
            timer = createTimer(millis)
        }
    }
    fun createTimer(millis : Long): Flow<Long> {
        return flow<Long> {
            var remainingMillis = millis

            while (remainingMillis > 0) {
                delay(1000)
                remainingMillis -= 1000
                if (remainingMillis <= 0){
//                    changeValues(1)
                }
            }
        }
    }

    override fun getRunningTimers(): Flow<Long> {
        var remainingMillis = totalMillis
        return timer!!
    }

    override fun endTimer(timer: Timer) {
    }

    override fun stopTimer(timer: Timer) {
    }

}