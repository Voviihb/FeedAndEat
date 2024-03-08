package com.vk_edu.feed_and_eat.features.login.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.vk_edu.feed_and_eat.features.login.domain.models.LoginForm

class LoginScreenViewModel : ViewModel() {
    private val _loginFormState = mutableStateOf(LoginForm("", ""))
    val loginFormState: State<LoginForm> = _loginFormState

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage


    private fun onError(message: String) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun emailChanged(value: String) {
        _loginFormState.value = _loginFormState.value.copy(
            email = value
        )
    }

    fun passwordChanged(value: String) {
        _loginFormState.value = _loginFormState.value.copy(
            password = value
        )
    }
}