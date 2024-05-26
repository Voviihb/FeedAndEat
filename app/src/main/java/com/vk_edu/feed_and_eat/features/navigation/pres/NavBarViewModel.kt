package com.vk_edu.feed_and_eat.features.navigation.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.vk_edu.feed_and_eat.PreferencesManager
import com.vk_edu.feed_and_eat.features.login.pres.getCurrentUserId
import com.vk_edu.feed_and_eat.features.navigation.models.BottomScreen
import com.vk_edu.feed_and_eat.features.navigation.models.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavBarViewModel @Inject constructor(
    private val _preferencesManager: PreferencesManager
) : ViewModel() {
    private val privateBottomDestination = mutableStateOf(BottomScreen.HomeScreen.route)
    val currentBottomState : State<String> = privateBottomDestination

    fun changeBottomDestination(value : String){
        privateBottomDestination.value = value
    }
    fun getStartDestination(): String {
        val data = getCurrentUserId(_preferencesManager)
        return if (data != null) BottomScreen.HomeScreen.route else Screen.LoginScreen.route
    }
}