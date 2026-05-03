package com.vk_edu.feed_and_eat.features.navigation.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.login.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavBarViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val privateBottomDestination = mutableStateOf(BottomScreen.HomeScreen.route)
    val currentBottomState: State<String> = privateBottomDestination

    private val _startDestination = mutableStateOf(Screen.LoginScreen.route)
    val startDestination: State<String> = _startDestination

    // Событие для принудительной навигации на экран входа (при истечении токена)
    private val _logoutEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val logoutEvent: SharedFlow<Unit> = _logoutEvent.asSharedFlow()

    private var wasAuthorized = false

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            val isAuthorized = authRepository.isAuthorized().firstOrNull() ?: false
            wasAuthorized = isAuthorized
            _startDestination.value = if (isAuthorized) {
                BottomScreen.HomeScreen.route
            } else {
                Screen.LoginScreen.route
            }

            // После определения начального состояния — подписываемся на изменения
            // drop(1) чтобы пропустить первое значение (уже обработали выше)
            authRepository.isAuthorized().drop(1).collect { isAuth ->
                if (wasAuthorized && !isAuth) {
                    // Токен протух или был очищен — отправляем событие выхода
                    _logoutEvent.emit(Unit)
                }
                wasAuthorized = isAuth
            }
        }
    }

    fun changeBottomDestination(value: String) {
        privateBottomDestination.value = value
    }

    fun getStartDestination(): String {
        return _startDestination.value
    }
}
