package com.vk_edu.feed_and_eat.features.navigation.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.login.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavBarViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val privateBottomDestination = mutableStateOf(BottomScreen.HomeScreen.route)
    val currentBottomState : State<String> = privateBottomDestination
    
    private val _startDestination = mutableStateOf(Screen.LoginScreen.route)
    val startDestination: State<String> = _startDestination

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            val isAuthorized = authRepository.isAuthorized().firstOrNull() ?: false
            _startDestination.value = if (isAuthorized) {
                BottomScreen.HomeScreen.route
            } else {
                Screen.LoginScreen.route
            }
        }
    }

    fun changeBottomDestination(value : String){
        privateBottomDestination.value = value
    }
    
    fun getStartDestination(): String {
        return _startDestination.value
    }
}