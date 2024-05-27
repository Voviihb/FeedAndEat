package com.vk_edu.feed_and_eat.features.profile.pres

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.PreferencesManager
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoImpl
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.login.pres.removeUserId
import com.vk_edu.feed_and_eat.features.navigation.pres.Screen
import com.vk_edu.feed_and_eat.features.profile.data.UsersRepoImpl
import com.vk_edu.feed_and_eat.features.profile.domain.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val _authRepo: AuthRepoImpl,
    private val _usersRepo: UsersRepoImpl,
    private val _preferencesManager: PreferencesManager
) : ViewModel() {
    private val _profileState = mutableStateOf(Profile(null, null, null, ""))
    val profileState: State<Profile> = _profileState

    private var _user = UserModel()

    private val _imagePath = mutableStateOf<Uri?>(null)
    val imagePath: State<Uri?> = _imagePath

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage


    fun loadProfileInfo() {
        viewModelScope.launch {
            try {
                val userId = _authRepo.getUserId()
                if (userId != null) {
                    _usersRepo.getUserData(userId).collect { response ->
                        when (response) {
                            is Response.Loading -> _loading.value = true
                            is Response.Success -> _user = response.data ?: UserModel()

                            is Response.Failure -> onError(response.e)
                        }
                    }
                }
                var nickname = _authRepo.getUserLogin()
                if (nickname == "") nickname = null
                var email = _authRepo.getUserEmail()
                if (email == "") email = null
                _profileState.value = _profileState.value.copy(
                    nickname = nickname,
                    email = email,
                    avatar = _user.avatarUrl,
                    aboutMe = _user.aboutMeData ?: ""
                )
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun logout(navigateToRoute: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _authRepo.signOut().collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            removeUserId(_preferencesManager)
                            navigateToRoute(Screen.LoginScreen.route)
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

    fun updateUserProfile() {
        viewModelScope.launch {
            try {
                val userId = _authRepo.getUserId()
                if (userId != null) {
                    _usersRepo.updateUserData(
                        userId,
                        profileState.value,
                        imagePath = imagePath.value
                    )
                        .collect { response ->
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


    fun aboutMeChanged(value: String) {
        _profileState.value = _profileState.value.copy(
            aboutMe = value
        )
    }

    fun imageChanged(value: Uri?) {
        _imagePath.value = value
    }


    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

    companion object {
        const val IMAGE_PATH_DIALOG = "\"image/*\""
    }
}