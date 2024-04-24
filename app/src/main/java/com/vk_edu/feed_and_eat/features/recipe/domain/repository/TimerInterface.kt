package com.vk_edu.feed_and_eat.features.recipe.domain.repository

import com.google.firebase.firestore.IgnoreExtraProperties
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Timer
import kotlinx.coroutines.flow.Flow

interface TimerInterface{
    fun startTimer(millis : Long)
    fun getRunningTimers():Flow<Long>
    fun endTimer(timer : Timer)
    fun stopTimer(timer: Timer)
}

@IgnoreExtraProperties
data class RunTimer(
    val id: Int,
    val type: String,
    val lowerLimit: Int?,
    val upperLimit: Int?,
    val number: Int?,
){
    constructor(id : Int, timer: Timer) : this(
        id,
        timer.type,
        timer.lowerLimit,
        timer.upperLimit,
        timer.number)
}