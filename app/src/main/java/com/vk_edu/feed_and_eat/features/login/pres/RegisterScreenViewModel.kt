package com.vk_edu.feed_and_eat.features.login.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.PreferencesManager
import com.vk_edu.feed_and_eat.features.collection.domain.models.Compilation
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoImpl
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.navigation.pres.BottomScreen
import com.vk_edu.feed_and_eat.features.profile.data.UsersRepoImpl
import com.vk_edu.feed_and_eat.features.profile.domain.models.UserModel
import com.vk_edu.feed_and_eat.features.recipe.data.models.RecipeDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val _authRepo: AuthRepoImpl,
    private val _usersRepo: UsersRepoImpl,
    private val _preferencesManager: PreferencesManager
) : ViewModel() {
    private val _registerFormState = mutableStateOf(RegisterForm("", "", "", ""))
    val registerFormState: State<RegisterForm> = _registerFormState

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage


    fun registerUserWithEmail(navigateToRoute: (String) -> Unit) {
        viewModelScope.launch {
            if (_registerFormState.value.password == _registerFormState.value.passwordControl) {
                try {
                    _authRepo.firebaseSignUp(
                        _registerFormState.value.email,
                        _registerFormState.value.password,
                        _registerFormState.value.login
                    ).collect { response ->
                        when (response) {
                            is Response.Loading -> _loading.value = true
                            is Response.Success -> {
                                val currentUserId = _authRepo.getUserId()
                                if (currentUserId != null) {
                                    writeUserId(_preferencesManager, currentUserId)
                                    saveUserData()
                                    navigateToRoute(BottomScreen.HomeScreen.route)
                                }
                            }

                            is Response.Failure -> onError(response.e)
                        }
                    }

                } catch (e: Exception) {
                    onError(e)
                }

            } else {
                onError(PasswordDiffersException())
            }
            _loading.value = false
        }

    }

    private fun saveUserData() {
        viewModelScope.launch {
            try {
                val userId = _authRepo.getUserId()
                if (userId != null) {
                    val data = UserModel(
                        userId = userId,
                        collections = listOf(
                            Compilation(
                                FAVOURITES,
                                mutableListOf<RecipeDataModel>()
                            )
                        )
                    )
                    _usersRepo.saveUserData(userId, data).collect { response ->
                        when (response) {
                            is Response.Loading -> _loading.value = true
                            is Response.Success -> {
                                /* TODO add success flow */
                            }

                            is Response.Failure -> {
                                onError(response.e)
                            }
                        }
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
            password = value
        )
    }

    fun password2Changed(value: String) {
        _registerFormState.value = _registerFormState.value.copy(
            passwordControl = value
        )
    }

    companion object {
        private const val FAVOURITES = "Favourites"
    }
}