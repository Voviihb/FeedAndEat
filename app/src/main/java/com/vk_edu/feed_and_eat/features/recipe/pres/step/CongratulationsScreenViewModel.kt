package com.vk_edu.feed_and_eat.features.recipe.pres.step

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.dishes.domain.repository.RecipesRepository
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Review
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CongratulationsScreenViewModel @Inject constructor(
    private val _recipesRepo: RecipesRepository
) : ViewModel() {
    private val _reviewState = mutableStateOf(Review("", 0.0))
    val reviewState: State<Review> = _reviewState

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    private var currentReview: Review? = null
    private var loadReviewJob: Job? = null

    fun saveReview(recipe: Recipe) {
        viewModelScope.launch {
            // Ждём завершения загрузки существующего отзыва, если она ещё идёт
            loadReviewJob?.join()
            try {
                if (recipe.id != null) {
                    if (currentReview == null) {
                        _recipesRepo.addNewReviewOnRecipe(recipe.id, _reviewState.value)
                            .collect { response ->
                                when (response) {
                                    is Response.Loading -> _loading.value = true
                                    is Response.Success -> {
                                        currentReview = _reviewState.value
                                    }
                                    is Response.Failure -> {
                                        onError(response.e)
                                    }
                                }
                            }
                    } else {
                        _recipesRepo.updateReviewOnRecipe(
                            id = recipe.id,
                            oldReview = currentReview!!,
                            newReview = _reviewState.value
                        ).collect { response ->
                            when (response) {
                                is Response.Loading -> _loading.value = true
                                is Response.Success -> {
                                    currentReview = _reviewState.value
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

    fun loadOldReview(recipe: Recipe) {
        if (recipe.id != null) {
            // Загружаем свой отзыв с сервера (аутентификация через Bearer token)
            loadReviewJob = viewModelScope.launch {
                try {
                    _recipesRepo.loadMyReviewOnRecipe(recipe.id).collect { response ->
                        when (response) {
                            is Response.Loading -> _loading.value = true
                            is Response.Success -> {
                                if (response.data != null) {
                                    currentReview = response.data
                                    _reviewState.value = response.data
                                } else {
                                    currentReview = null
                                }
                            }
                            is Response.Failure -> {
                                // Нет отзыва — это нормально
                                currentReview = null
                            }
                        }
                    }
                } catch (e: Exception) {
                    currentReview = null
                }
                _loading.value = false
            }
        }
    }

    fun incrementCookedField(recipe: Recipe) {
        viewModelScope.launch {
            try {
                if (recipe.id != null) {
                    _recipesRepo.incrementCookedCounter(recipe.id)
                        .collect { response ->
                            when (response) {
                                is Response.Loading -> _loading.value = true
                                is Response.Success -> { /* счётчик обновлён */ }
                                is Response.Failure -> onError(response.e)
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

    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
