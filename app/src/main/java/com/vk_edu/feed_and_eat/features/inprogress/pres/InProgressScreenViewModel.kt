package com.vk_edu.feed_and_eat.features.inprogress.pres

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
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
class InProgressScreenViewModel @Inject constructor(
    @ApplicationContext appContext: Context
) : ViewModel() {
    private val application = Contexts.getApplication(appContext)
    private val timerServiceConnection = TimerServiceConnection()

    val activeTimerState = timerServiceConnection.activeTimerUpdates

    init {
        val intent = Intent(application, TimerService::class.java)
        application.bindService(intent, timerServiceConnection, Context.BIND_ABOVE_CLIENT)
    }

    override fun onCleared() {
        super.onCleared()
        application.unbindService(timerServiceConnection)
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