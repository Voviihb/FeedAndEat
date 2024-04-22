package com.vk_edu.feed_and_eat.features.recipe.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.dishes.data.RecipesRepoImpl
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesScreenViewModel @Inject constructor(
    private val _recipesRepo: RecipesRepoImpl
) : ViewModel() {
    private val privateRecipe = mutableStateOf(Recipe())
    var recipe : State<Recipe> = privateRecipe

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    fun loadRecipeById(id: String) {
        viewModelScope.launch {
            try {
                _recipesRepo.loadRecipeById(id).collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            if (response.data != null) {
                                privateRecipe.value = response.data
                            }
                        }

                        is Response.Failure -> {
                            onError(response.e)
                        }
                    }
                }

            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}