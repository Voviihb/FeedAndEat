package com.vk_edu.feed_and_eat.features.login.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoImpl
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    private val _loginFormState = mutableStateOf(LoginForm("", ""))
    val loginFormState: State<LoginForm> = _loginFormState

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    private val _auth: FirebaseAuth = Firebase.auth
    private val _authRepo = AuthRepoImpl(_auth)

    private val _signInState = mutableStateOf(false)
    val signInState: State<Boolean> = _signInState

    val isUserAuthenticated get() = _authRepo.isUserAuthenticatedInFirebase()

    fun loginWithEmail() {
        viewModelScope.launch {
            try {
                _authRepo.firebaseSignIn(
                    _loginFormState.value.email,
                    _loginFormState.value.password
                ).collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> _signInState.value = true
                        is Response.Failure -> onError(response.e)
                    }
                }

            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }

    }

    fun logout() {
        viewModelScope.launch {
            try {
                _authRepo.signOut().collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> _signInState.value = false
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