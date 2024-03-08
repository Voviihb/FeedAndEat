package com.vk_edu.feed_and_eat.features.login.pres

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoImpl
import com.vk_edu.feed_and_eat.features.login.domain.models.RegisterForm
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import kotlinx.coroutines.launch

class RegisterScreenViewModel : ViewModel() {
    private val EXCEPTION_OCCURRED = "Exception occurred!"
    private val PASSWORD_DIFFERS = "Passwords are not equal!"

    private val _registerFormState = mutableStateOf(RegisterForm("", "", "", ""))
    val registerFormState: State<RegisterForm> = _registerFormState

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    private val auth: FirebaseAuth = Firebase.auth
    private val authRepo = AuthRepoImpl(auth)

    private val _signUpState = mutableStateOf(false)
    val signUpState: State<Boolean> = _signUpState

    fun registerUserWithEmail() {
        viewModelScope.launch {
            if (_registerFormState.value.password1 == _registerFormState.value.password2) {
                try {
                    authRepo.firebaseSignUp(
                        _registerFormState.value.email,
                        _registerFormState.value.password1
                    ).collect { response ->
                        when (response) {
                            is Response.Loading -> _loading.value = true
                            is Response.Success -> _signUpState.value = true
                            is Response.Failure -> onError(response.e.message ?: EXCEPTION_OCCURRED)
                        }
                        Log.d("Taag", response.toString())
                    }

                } catch (e: Exception) {
                    onError(e.message ?: EXCEPTION_OCCURRED)
                }

            } else {
                onError(PASSWORD_DIFFERS)
            }

        }
    }

    private fun onError(message: String) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun emailChanged(value: String) {
        _registerFormState.value = _registerFormState.value.copy(
            email = value
        )
    }

    fun loginChanged(value: String) {
        _registerFormState.value = _registerFormState.value.copy(
            login = value
        )
    }

    fun password1Changed(value: String) {
        _registerFormState.value = _registerFormState.value.copy(
            password1 = value
        )
    }

    fun password2Changed(value: String) {
        _registerFormState.value = _registerFormState.value.copy(
            password2 = value
        )
    }
}