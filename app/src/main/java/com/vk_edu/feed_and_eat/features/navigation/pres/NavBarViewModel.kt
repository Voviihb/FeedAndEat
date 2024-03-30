package com.vk_edu.feed_and_eat.features.navigation.pres

import androidx.lifecycle.ViewModel
import com.vk_edu.feed_and_eat.PreferencesManager
import com.vk_edu.feed_and_eat.features.login.pres.getCurrentUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavBarViewModel @Inject constructor(
    private val _preferencesManager: PreferencesManager
) : ViewModel() {
    fun getStartDestination(): String {
        val data = getCurrentUserId(_preferencesManager)
        return if (data != null) Screen.HomeScreen.route else Screen.LoginScreen.route
    }
}