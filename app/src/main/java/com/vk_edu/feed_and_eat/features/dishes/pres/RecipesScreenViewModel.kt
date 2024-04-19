package com.vk_edu.feed_and_eat.features.dishes.pres

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
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
    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    private val _isLoadSuccess = mutableStateOf<Boolean>(false)
    val isLoadSuccess: State<Boolean> = _isLoadSuccess

    private val _recipesList = mutableStateOf(listOf<Recipe>())
    val recipesList: State<List<Recipe>> = _recipesList

    /**
     * Stores last seen document. Is used to calculate offset for pagination
     * */
    private var _prevDocument: DocumentSnapshot? = null

    /**
     * Loads all recipes with pagination, order by DocID. Returns new collection every call.
     * Uses loadRecipes() from repo
     * */
    fun loadRecipes() {
        viewModelScope.launch {
            try {
                _recipesRepo.loadRecipes(prevDocument = _prevDocument).collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            _recipesList.value += response.data.recipes
                            _prevDocument = response.data.prevDocument
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


    fun loadRecipeById(id: String) {
        viewModelScope.launch {
            try {
                _recipesRepo.loadRecipeById(id).collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            if (response.data != null) {
                                _recipesList.value = listOf(response.data)
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

    fun filterRecipes(
        sort: String,
        limit: Long,
        includedIngredients: List<String> = listOf(),
        excludedIngredients: List<String> = listOf(),
        tags: List<String> = listOf(),
        caloriesMin: Double = 0.0,
        caloriesMax: Double = 10e9,
        sugarMin: Double = 0.0,
        sugarMax: Double = 10e9,
        proteinMin: Double = 0.0,
        proteinMax: Double = 10e9,
        fatMin: Double = 0.0,
        fatMax: Double = 10e9,
        carbohydratesMin: Double = 0.0,
        carbohydratesMax: Double = 10e9
    ) {
        viewModelScope.launch {
            try {
                _recipesRepo.filterRecipes(
                    sort,
                    limit,
                    _prevDocument,
                    includedIngredients,
                    excludedIngredients,
                    tags,
                    caloriesMin,
                    caloriesMax,
                    sugarMin,
                    sugarMax,
                    proteinMin,
                    proteinMax,
                    fatMin,
                    fatMax,
                    carbohydratesMin,
                    carbohydratesMax
                ).collect { response ->
                    Log.d("Taag", response.toString())
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            _recipesList.value += response.data.recipes
                            _prevDocument = response.data.prevDocument
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
}