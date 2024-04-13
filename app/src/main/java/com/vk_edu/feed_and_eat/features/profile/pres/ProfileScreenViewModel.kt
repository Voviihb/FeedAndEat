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

    fun logout(/*TODO pass navigation func to login after merge*/) {
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

    fun updateUserProfile() {
        viewModelScope.launch {
            try {
                val userId = _authRepo.getUserId()
                val profileType = when (_selectedProfileType.value) {
                    ProfileType.PUBLIC -> false
                    ProfileType.PRIVATE -> true
                }
                val theme = when (_selectedTheme.value) {
                    ThemeSelection.LIGHT -> LIGHT_VALUE
                    ThemeSelection.DARK -> DARK_VALUE
                    ThemeSelection.AS_SYSTEM -> AS_SYSTEM_VALUE
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
        private const val LIGHT_VALUE = "light"
        private const val DARK_VALUE = "dark"
        private const val AS_SYSTEM_VALUE = "system"

        private const val AVATAR_URL_VALUE = "avatarUrl"
        private const val ABOUT_ME_VALUE = "aboutMeData"
        private const val IS_PROFILE_PRIVATE_VALUE = "isProfilePrivate"
        private const val THEME_SETTINGS_VALUE = "themeSettings"
    }
}