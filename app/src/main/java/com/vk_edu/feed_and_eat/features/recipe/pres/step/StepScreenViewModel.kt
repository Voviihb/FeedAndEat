package com.vk_edu.feed_and_eat.features.recipe.pres.step

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.recipe.domain.TimerService
import com.vk_edu.feed_and_eat.features.recipe.domain.TimerState
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class StepScreenViewModel @Inject constructor(
    @ApplicationContext appContext: Context
): ViewModel(){
    val name = mutableStateOf("")
    val id = mutableIntStateOf(0)
    var recipeId: MutableState<String?> = mutableStateOf(null)

    private var privateTimerMap = mutableMapOf<String, MutableState<Int>>()
    val currentTimerMap : Map<String, MutableState<Int>> = privateTimerMap

    private val application = Contexts.getApplication(appContext)
    private val timerServiceConnection = TimerServiceConnection()

    private val privateTimerFlag = mutableMapOf<String, MutableState<Boolean>>()
    val runTimerFlag : Map<String, MutableState<Boolean>> = privateTimerFlag

    private val privateRunning = mutableMapOf<String, MutableState<Boolean>>()
    val isRunning : Map<String, MutableState<Boolean>> = privateRunning

    private val privateSlider = mutableMapOf<String, MutableState<Long>>()
    val sliderPosition : Map<String, MutableState<Long>> = privateSlider

    val activeTimerState = timerServiceConnection.activeTimerUpdates

    init {
        val intent = Intent(application, TimerService::class.java)
        application.bindService(intent, timerServiceConnection, Context.BIND_ABOVE_CLIENT)
    }

    override fun onCleared() {
        super.onCleared()
        application.unbindService(timerServiceConnection)
    }

    fun revertInit(name: String, pause : Boolean){
        privateTimerFlag[name] = mutableStateOf(false)
        privateRunning[name] = mutableStateOf(pause)
        privateSlider[name] = mutableLongStateOf(0L)
    }

    private fun changeTimerFlag(name: String){
        privateTimerFlag.getValue(name).value = !privateTimerFlag.getValue(name).value
    }

    private fun changeTimerState(name : String){
        privateRunning.getValue(name).value = !privateRunning.getValue(name).value
    }

    fun changeInit(name: String){
        privateTimerFlag[name] = mutableStateOf(true)
        privateRunning[name] = mutableStateOf(false)
        privateSlider[name] = mutableLongStateOf(0L)
    }

    fun changeSliderValue(name : String, value : Long){
        privateSlider.getValue(name).value = value
    }

    var iterateTimerOrder : (String, Int) -> Unit = {name: String, value : Int ->
        privateTimerMap[name]?.value = currentTimerMap[name]?.value?.plus(value) ?: value
    }

    var changeTimerOrder : (String, Int) -> Unit = { name : String, value : Int ->
        privateTimerMap[name]?.value = value
    }

    fun initTimer(name: String){
        privateTimerMap[name] = mutableIntStateOf(0)
    }

    fun clear(name : String) {
        currentTimerMap[name]?.value = 0
    }

    fun startTimer(
        name: String,
        time: Int,
    ) {
        changeTimerFlag(name)
        changeTimerState(name)
        val timerService = Intent(application, TimerService::class.java)
        timerService.putExtra(
            TimerService.ACTION,
            TimerService.ACTION_START
        )
        timerService.putExtra(
            TimerService.TIMER_ID,
            name
        )
        timerService.putExtra(
            TimerService.TIMER_TIME,
            time
        )
        timerService.putExtra(
            TimerService.RECIPE_ID,
            recipeId.value
        )
        application.startService(timerService)
    }

    fun stopTimer(name: String) {
        privateRunning.remove(name)
        privateTimerFlag.remove(name)
        val timerService = Intent(application, TimerService::class.java)
        timerService.putExtra(
            TimerService.ACTION,
            TimerService.ACTION_STOP
        )
        timerService.putExtra(
            TimerService.TIMER_ID,
            name
        )
        application.startService(timerService)
    }

    fun pauseTimer(name: String) {
        changeTimerState(name)
        val timerService = Intent(application, TimerService::class.java)
        timerService.putExtra(
            TimerService.ACTION,
            TimerService.ACTION_PAUSE
        )
        timerService.putExtra(
            TimerService.TIMER_ID,
            name
        )
        application.startService(timerService)
    }

    fun resumeTimer(name: String) {
        changeTimerState(name)
        val timerService = Intent(application, TimerService::class.java)
        timerService.putExtra(
            TimerService.ACTION,
            TimerService.ACTION_RESUME
        )
        timerService.putExtra(
            TimerService.TIMER_ID,
            name
        )
        application.startService(timerService)
    }

    inner class TimerServiceConnection : ServiceConnection {
        private val _activeTimerUpdates = MutableStateFlow<Map<String, TimerState>>(emptyMap())
        val activeTimerUpdates: StateFlow<Map<String, TimerState>> = _activeTimerUpdates

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as TimerService.LocalBinder
            binder.getService().activeTimerUpdates.onEach { update ->
                _activeTimerUpdates.emit(update)
            }.launchIn(viewModelScope)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }


}