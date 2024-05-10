package com.vk_edu.feed_and_eat.features.main.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.dishes.data.RecipesRepoImpl
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val _recipesRepo: RecipesRepoImpl
) : ViewModel() {
    private val _largeCardData = mutableStateOf(RecipeCard())
    var largeCardData: State<RecipeCard> = _largeCardData

    private val _cardsDataOfRow1 = mutableStateOf(listOf<RecipeCard>())
    var cardsDataOfRow1: State<List<RecipeCard>> = _cardsDataOfRow1

    private val _cardsDataOfRow2 = mutableStateOf(listOf<RecipeCard>())
    var cardsDataOfRow2: State<List<RecipeCard>> = _cardsDataOfRow2

    private val _cardsDataOfRow3 = mutableStateOf(listOf<RecipeCard>())
    var cardsDataOfRow3: State<List<RecipeCard>> = _cardsDataOfRow3

    private val _cardsDataOfRow4 = mutableStateOf(listOf<RecipeCard>())
    var cardsDataOfRow4: State<List<RecipeCard>> = _cardsDataOfRow4

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    fun getLargeCardData() {
        viewModelScope.launch {
            try {
                _recipesRepo.loadDailyRecipe().collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            if (response.data != null) {
                                val fullRecipe = response.data
                                _largeCardData.value = RecipeCard(
                                    recipeId = fullRecipe.id ?: "",
                                    image = fullRecipe.image ?: "",
                                    ingredients = fullRecipe.ingredients.size,
                                    steps = fullRecipe.instructions.size,
                                    name = fullRecipe.name,
                                    rating = fullRecipe.rating,
                                    cooked = fullRecipe.cooked
                                )
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

    fun getCardsDataOfRow1() {
        viewModelScope.launch {
            try {
                _recipesRepo.loadTopRatingRecipes().collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            _cardsDataOfRow1.value = response.data.map { fullRecipe ->
                                RecipeCard(
                                    recipeId = fullRecipe.id ?: "",
                                    image = fullRecipe.image ?: "",
                                    ingredients = fullRecipe.ingredients.size,
                                    steps = fullRecipe.instructions.size,
                                    name = fullRecipe.name,
                                    rating = fullRecipe.rating,
                                    cooked = fullRecipe.cooked
                                )
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

    fun getCardsDataOfRow2() {
        viewModelScope.launch {
            try {
                _recipesRepo.loadLowCalorieRecipes().collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            _cardsDataOfRow2.value = response.data.map { fullRecipe ->
                                RecipeCard(
                                    recipeId = fullRecipe.id ?: "",
                                    image = fullRecipe.image ?: "",
                                    ingredients = fullRecipe.ingredients.size,
                                    steps = fullRecipe.instructions.size,
                                    name = fullRecipe.name,
                                    rating = fullRecipe.rating,
                                    cooked = fullRecipe.cooked
                                )
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

    fun getCardsDataOfRow3() {
        viewModelScope.launch {
            try {
                _recipesRepo.loadLastAddedRecipes().collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            _cardsDataOfRow3.value = response.data.map { fullRecipe ->
                                RecipeCard(
                                    recipeId = fullRecipe.id ?: "",
                                    image = fullRecipe.image ?: "",
                                    ingredients = fullRecipe.ingredients.size,
                                    steps = fullRecipe.instructions.size,
                                    name = fullRecipe.name,
                                    rating = fullRecipe.rating,
                                    cooked = fullRecipe.cooked
                                )
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

    fun getCardsDataOfRow4() {
        viewModelScope.launch {
            try {
                _recipesRepo.loadBreakfastRecipes().collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            _cardsDataOfRow4.value = response.data.map { fullRecipe ->
                                RecipeCard(
                                    recipeId = fullRecipe.id ?: "",
                                    image = fullRecipe.image ?: "",
                                    ingredients = fullRecipe.ingredients.size,
                                    steps = fullRecipe.instructions.size,
                                    name = fullRecipe.name,
                                    rating = fullRecipe.rating,
                                    cooked = fullRecipe.cooked
                                )
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