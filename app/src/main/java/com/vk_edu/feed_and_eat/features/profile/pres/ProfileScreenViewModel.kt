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

    val themes = listOf(ThemeSelection.LIGHT, ThemeSelection.DARK, ThemeSelection.AS_SYSTEM)
    private val _selectedTheme = mutableStateOf(themes[0])
    val selectedTheme: State<ThemeSelection> = _selectedTheme

    val profileType = listOf(ProfileType.PUBLIC, ProfileType.PRIVATE)
    private val _selectedProfileType = mutableStateOf(profileType[0])
    val selectedProfileType: State<ProfileType> = _selectedProfileType


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

    fun logout() {
        /* TODO copy impl from LoginScreenViewModel */
    }

    fun saveUserData() {
        /* TODO make after merge with feature_DataStore */
    }

    fun aboutMeChanged(value: String) {
        _profileState.value = _profileState.value.copy(
            aboutMe = value
        )
    }

    fun onThemeOptionSelected(theme: ThemeSelection) {
        _selectedTheme.value = theme
    }

    fun onProfileOptionSelected(profileType: ProfileType) {
        _selectedProfileType.value = profileType
    }


    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

}