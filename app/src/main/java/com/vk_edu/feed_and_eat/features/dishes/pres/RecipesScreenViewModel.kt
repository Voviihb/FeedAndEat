package com.vk_edu.feed_and_eat.features.dishes.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
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

    private val _recipesList = mutableStateListOf<Recipe>()
    val recipesList: List<Recipe> = _recipesList

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
                            /* TODO remove list clear if needed */
                            if (_recipesList.isNotEmpty()) {
                                _recipesList.clear()
                            }
                            _recipesList += response.data.first
                            _prevDocument = response.data.second
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

    /**
     * Loads one recipe by its id. Uses loadRecipeById(id) from repo
     * */
    fun loadRecipeById(id: String) {
        viewModelScope.launch {
            try {
                _recipesRepo.loadRecipeById(id).collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            if (_recipesList.isNotEmpty()) {
                                _recipesList.clear()
                            }
                            if (response.data != null) {
                                _recipesList += response.data
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
}