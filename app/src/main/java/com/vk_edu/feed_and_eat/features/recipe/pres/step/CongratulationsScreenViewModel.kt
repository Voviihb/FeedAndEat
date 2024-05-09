package com.vk_edu.feed_and_eat.features.recipe.pres.step

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.dishes.data.RecipesRepoImpl
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Review
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoImpl
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CongratulationsScreenViewModel @Inject constructor(
    private val _authRepo: AuthRepoImpl,
    private val _recipesRepo: RecipesRepoImpl
) : ViewModel() {
    private val _reviewState = mutableStateOf(Review("", 0.0))
    val reviewState: State<Review> = _reviewState

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    fun saveReview(recipe: Recipe) {
        viewModelScope.launch {
            try {
                val user = _authRepo.getUserId()
                if (recipe.id != null && user != null) {
                    authorChanged(user)
                    val oldReview: Review? = recipe.reviews?.filter { it.author == user }?.get(0)
                    if (oldReview == null) {
                        _recipesRepo.addNewReviewOnRecipe(recipe.id, _reviewState.value)
                            .collect { response ->
                                Log.d("Taag", response.toString())
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
                    } else {
                        _recipesRepo.updateReviewOnRecipe(
                            id = recipe.id,
                            oldReview = oldReview,
                            newReview = _reviewState.value
                        )
                            .collect { response ->
                                Log.d("Taaag", response.toString())
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

                }


            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun markChanged(value: Float) {
        _reviewState.value = _reviewState.value.copy(
            mark = value.toDouble()
        )
    }

    private fun authorChanged(value: String) {
        _reviewState.value = _reviewState.value.copy(
            author = value
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