package com.vk_edu.feed_and_eat.features.recipe.pres

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class StepScreenViewModel @Inject constructor(

): ViewModel(){
    var sliderPosition : MutableState<Long> = mutableStateOf(0L)

    var remainingMillis : MutableState<Long> = mutableStateOf(0L)

    var isRunning : MutableState<Boolean> = mutableStateOf(false)

    var job : MutableState<Job?> = mutableStateOf<Job?>(null)

    var displaySlider : MutableState<Boolean> = mutableStateOf(true)

    var currentTimer : MutableState<Int> = mutableStateOf(0)

    var changeValue : (Int) -> Unit = {currentTimer.value += it}

    fun setRemainingMillis(value : Long){
            remainingMillis.value = value
    }
    fun dropTimers(){
        currentTimer.value = 0
    }
    fun clear(){
        currentTimer.value = 0
        remainingMillis.value = 0L
        isRunning.value = false
        sliderPosition.value = 0L
        job.value = null
    }
}