package com.vk_edu.feed_and_eat.features.cooking.pres

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.cooking.domain.TimerService
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CookingScreenViewModel @Inject constructor(
    @ApplicationContext appContext: Context
) : ViewModel() {
    private val application = getApplication(appContext)
    private val timerServiceConnection = TimerServiceConnection()

    val activeTimerState = timerServiceConnection.activeTimerUpdates
    val pausedTimerState = timerServiceConnection.pausedTimerUpdates

    private val _counter = mutableIntStateOf(1)
    val counter: State<Int> = _counter

    init {
        val intent = Intent(application, TimerService::class.java)
        application.bindService(intent, timerServiceConnection, Context.BIND_ABOVE_CLIENT)
        Log.d("Taaag", "VM created")
    }

    override fun onCleared() {
        super.onCleared()
        application.unbindService(timerServiceConnection)
    }

    fun startTimer(timerId: String, time: Int) {
        val timerService = Intent(application, TimerService::class.java)
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
        application.startService(timerService)
    }

    fun stopTimer(timerId: String) {
        val timerService = Intent(application, TimerService::class.java)
        timerService.putExtra(
            TimerService.ACTION,
            TimerService.ACTION_STOP
        )
        timerService.putExtra(
            TimerService.TIMER_ID,
            timerId
        )
        application.startService(timerService)
    }

    fun pauseTimer(timerId: String) {
        val timerService = Intent(application, TimerService::class.java)
        timerService.putExtra(
            TimerService.ACTION,
            TimerService.ACTION_PAUSE
        )
        timerService.putExtra(
            TimerService.TIMER_ID,
            timerId
        )
        application.startService(timerService)
    }

    fun resumeTimer(timerId: String) {
        val timerService = Intent(application, TimerService::class.java)
        timerService.putExtra(
            TimerService.ACTION,
            TimerService.ACTION_RESUME
        )
        timerService.putExtra(
            TimerService.TIMER_ID,
            timerId
        )
        application.startService(timerService)
    }

    fun updateCounter(value: Int) {
        _counter.intValue += value
    }


    inner class TimerServiceConnection : ServiceConnection {
        private val _activeTimerUpdates = MutableSharedFlow<Map<String, Int>>(replay = 1)
        val activeTimerUpdates: SharedFlow<Map<String, Int>> = _activeTimerUpdates

        private val _pausedTimerUpdates = MutableSharedFlow<Map<String, Int>>(replay = 1)
        val pausedTimerUpdates: SharedFlow<Map<String, Int>> = _pausedTimerUpdates

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as TimerService.LocalBinder
            binder.getService().activeTimerUpdates.onEach { update ->
                _activeTimerUpdates.emit(update)
            }.launchIn(viewModelScope)
            binder.getService().pausedTimerUpdates.onEach { update ->
                _pausedTimerUpdates.emit(update)
            }.launchIn(viewModelScope)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }
}