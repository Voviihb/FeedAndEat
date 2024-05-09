package com.vk_edu.feed_and_eat.features.recipe.pres.step

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Review
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoImpl
import com.vk_edu.feed_and_eat.features.profile.data.UsersRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CongratulationsScreenViewModel @Inject constructor(
    private val _authRepo: AuthRepoImpl,
    private val _usersRepo: UsersRepoImpl
) : ViewModel() {
    private val _reviewState = mutableStateOf(Review(0, 0.0, null))
    val reviewState: State<Review> = _reviewState

    fun markChanged(value: Float) {
        _reviewState.value = _reviewState.value.copy(
            mark = value.toDouble()
        )
    }

    fun messageChanged(value: String) {
        _reviewState.value = _reviewState.value.copy(
            message = value
        )
    }
}