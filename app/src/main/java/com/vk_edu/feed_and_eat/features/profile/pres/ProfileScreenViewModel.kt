package com.vk_edu.feed_and_eat.features.profile.pres

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

    val themes = ThemeSelection.entries.toTypedArray()
    private val _selectedTheme = mutableStateOf(themes[0])
    val selectedTheme: State<ThemeSelection> = _selectedTheme

    val profileType = ProfileType.entries.toTypedArray()
    private val _selectedProfileType = mutableStateOf(profileType[0])
    val selectedProfileType: State<ProfileType> = _selectedProfileType

    private val _user = mutableStateOf(UserModel())

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
                            is Response.Success -> if (response.data != null) {
                                _user.value = response.data
                            } else {
                                _user.value = UserModel()
                            }

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
                    avatar = _user.value.avatarUrl,
                    aboutMe = _user.value.aboutMeData ?: ""
                )

                _selectedTheme.value = when (_user.value.themeSettings) {
                    ThemeSelection.LIGHT.themeName -> ThemeSelection.LIGHT
                    ThemeSelection.DARK.themeName -> ThemeSelection.DARK
                    ThemeSelection.AS_SYSTEM.themeName -> ThemeSelection.AS_SYSTEM
                    else -> ThemeSelection.LIGHT
                }

                _selectedProfileType.value = when (_user.value.isProfilePrivate) {
                    ProfileType.PUBLIC.type -> ProfileType.PUBLIC
                    ProfileType.PRIVATE.type -> ProfileType.PRIVATE
                    else -> ProfileType.PUBLIC
                }
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
                val profileType = when (_selectedProfileType.value) {
                    ProfileType.PUBLIC -> ProfileType.PUBLIC.type
                    ProfileType.PRIVATE -> ProfileType.PRIVATE.type
                }
                val theme = when (_selectedTheme.value) {
                    ThemeSelection.LIGHT -> ThemeSelection.LIGHT.themeName
                    ThemeSelection.DARK -> ThemeSelection.DARK.themeName
                    ThemeSelection.AS_SYSTEM -> ThemeSelection.AS_SYSTEM.themeName
                }
                if (userId != null) {
                    val data: HashMap<String, Any?> = hashMapOf(
                        AVATAR_URL_VALUE to _profileState.value.avatar,
                        ABOUT_ME_VALUE to _profileState.value.aboutMe,
                        IS_PROFILE_PRIVATE_VALUE to profileType,
                        THEME_SETTINGS_VALUE to theme
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

    companion object {
        private const val AVATAR_URL_VALUE = "avatarUrl"
        private const val ABOUT_ME_VALUE = "aboutMeData"
        private const val IS_PROFILE_PRIVATE_VALUE = "isProfilePrivate"
        private const val THEME_SETTINGS_VALUE = "themeSettings"
    }
}