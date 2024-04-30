package com.vk_edu.feed_and_eat.features.recipe.domain.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.vk_edu.feed_and_eat.features.recipe.pres.step.StepScreenViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerImpl(val viewModel: StepScreenViewModel) : TimerInterface{
    var job = mutableStateOf<Job?>(null)
    override fun startJob(){
        if (Job != null){
            job.value = createJob()
        }
    }

    private fun createJob() : Job {
        var remainingMillis by viewModel.remainingMillis
        var isRunning by viewModel.isRunning

        return GlobalScope.launch{
            while ((remainingMillis > 0) && (isRunning)) {
                remainingMillis -= 1000
                delay(1000)
                if (remainingMillis <= 0){
                    viewModel.changeValue(1)
                }
            }
        }
    }

    override fun getRunningJobs(): Job {
        try {
            return job.value!!
        } finally {
//            Log.d("Timer", job.toString())
        }
    }

    override fun endJob(){
        viewModel.isRunning.value = false
        job.value?.cancel()
    }

}