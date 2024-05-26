package com.vk_edu.feed_and_eat.features.login.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.PreferencesManager
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoImpl
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.navigation.models.BottomScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val _authRepo: AuthRepoImpl,
    private val _preferencesManager: PreferencesManager
) : ViewModel() {
    private val _loginFormState = mutableStateOf(LoginForm("", ""))
    val loginFormState: State<LoginForm> = _loginFormState

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage


    fun loginWithEmail(navigateToRoute: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _authRepo.firebaseSignIn(
                    _loginFormState.value.email,
                    _loginFormState.value.password
                ).collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            val currentUserId = _authRepo.getUserId()
                            if (currentUserId != null) {
                                writeUserId(_preferencesManager, currentUserId)
                                navigateToRoute(BottomScreen.HomeScreen.route)
                            }
                        }
                        is Response.Failure -> onError(response.e)
                    }
                }

            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }

    }

    fun signInAnonymously(navigateToRoute: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _authRepo.firebaseSignInAnonymously()
                .collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            val currentUserId = _authRepo.getUserId()
                            if (currentUserId != null) {
                                writeUserId(_preferencesManager, currentUserId)
                                navigateToRoute(BottomScreen.HomeScreen.route)
                            }
                        }
                        is Response.Failure -> onError(response.e)
                    }
                }

            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }

    }


    private fun onError(message: Exception?) {
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