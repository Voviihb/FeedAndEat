package com.vk_edu.feed_and_eat.features.profile.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.PreferencesManager
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoImpl
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.login.pres.removeUserId
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

    val themes = listOf(ThemeSelection.LIGHT, ThemeSelection.DARK, ThemeSelection.AS_SYSTEM)
    private val _selectedTheme = mutableStateOf(themes[0])
    val selectedTheme: State<ThemeSelection> = _selectedTheme

    val profileType = listOf(ProfileType.PUBLIC, ProfileType.PRIVATE)
    private val _selectedProfileType = mutableStateOf(profileType[0])
    val selectedProfileType: State<ProfileType> = _selectedProfileType

    private val _user = mutableStateOf(UserModel())

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
                val userId = _authRepo.getUserId()
                if (userId != null) {
                    _usersRepo.getUserData(userId).collect { response ->
                        when (response) {
                            is Response.Loading -> _loading.value = true
                            is Response.Success -> if (response.data != null) {
                                _user.value = response.data
                            }

                            is Response.Failure -> onError(response.e)
                        }
                    }
                }

                _profileState.value = _profileState.value.copy(
                    nickname = _authRepo.getUserLogin(),
                    email = "TEMPTEMP", /* TODO _authRepo.getUserEmail() */
                    avatar = _user.value.avatarUrl,
                    aboutMe = _user.value.aboutMeData ?: ""
                )
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun logout(/*TODO pass navigation func to login*/) {
        viewModelScope.launch {
            try {
                _authRepo.signOut().collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> removeUserId(_preferencesManager)
                        is Response.Failure -> onError(response.e)
                    }
                }
            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    /* TODO move this func to registration and invoke it */
    fun saveUserData() {
        viewModelScope.launch {
            try {
                val userId = _authRepo.getUserId()
                if (userId != null) {
                    val data = UserModel(userId = userId)
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

    fun updateUserProfile() {
        viewModelScope.launch {
            try {
                val userId = _authRepo.getUserId()
                val profileType = when (_selectedProfileType.value) {
                    ProfileType.PUBLIC -> false
                    ProfileType.PRIVATE -> true
                }
                val theme = when (_selectedTheme.value) {
                    ThemeSelection.LIGHT -> "light"
                    ThemeSelection.DARK -> "dark"
                    ThemeSelection.AS_SYSTEM -> "system"
                }
                if (userId != null) {
                    val data: HashMap<String, Any?> = hashMapOf(
                        "avatarUrl" to _profileState.value.avatar,
                        "aboutMeData" to _profileState.value.aboutMe,
                        "isProfilePrivate" to profileType,
                        "themeSettings" to theme
                    )

                    _usersRepo.updateUserData(userId, data)
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