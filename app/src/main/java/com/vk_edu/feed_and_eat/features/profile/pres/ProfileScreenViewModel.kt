package com.vk_edu.feed_and_eat.features.profile.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val _authRepo: AuthRepoImpl
) : ViewModel() {
    private val _profileState = mutableStateOf(Profile("", "", "", ""))
    val profileState: State<Profile> = _profileState

    private val _settingsState = mutableStateOf(Settings(ThemeSelection.LIGHT, ProfileType.PUBLIC))
    val settingsState: State<Settings> = _settingsState

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    init {
        loadProfileInfo()
    }

    private fun loadProfileInfo() {
        viewModelScope.launch {
            try {
                _loading.value = true
                _profileState.value = _profileState.value.copy(
                    nickname = _authRepo.getUserLogin() ?: "No nickname",
                    email = _authRepo.getUserEmail() ?: "No email"
                )
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun aboutMeChanged(value: String) {
        _profileState.value = _profileState.value.copy(
            aboutMe = value
        )
    }

    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

}