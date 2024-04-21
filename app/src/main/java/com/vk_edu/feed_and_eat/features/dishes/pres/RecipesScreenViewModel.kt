package com.vk_edu.feed_and_eat.features.dishes.pres

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.vk_edu.feed_and_eat.features.dishes.data.RecipesRepoImpl
import com.vk_edu.feed_and_eat.features.dishes.domain.models.FiltersDTO
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

    private var _startOfNextDocument: DocumentSnapshot? = null
    private var _endOfPrevDocument: DocumentSnapshot? = null

    fun loadRecipes(limit: Long, direction: String = FORWARD) {
        viewModelScope.launch {
            try {
                if (
                    (_startOfNextDocument == null && _endOfPrevDocument == null) ||
                    (direction == FORWARD && _startOfNextDocument != null) ||
                    (direction == BACK && _endOfPrevDocument != null)
                    ) _recipesRepo.loadRecipes(
                        limit = limit,
                        endOfPrevDocument = if (direction == BACK) _endOfPrevDocument else null,
                        startOfNextDocument = if (direction == FORWARD) _startOfNextDocument else null
                    ).collect { response ->
                        when (response) {
                            is Response.Loading -> _loading.value = true
                            is Response.Success -> {
                                if (response.data.recipes.isEmpty()) {
                                    if (direction == FORWARD)
                                        _startOfNextDocument = null
                                    else
                                        _endOfPrevDocument = null
                                }
                                else {
                                    _recipesList.value = response.data.recipes
                                    _startOfNextDocument = response.data.startOfNextDocument
                                    _endOfPrevDocument = response.data.endOfPrevDocument
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
        filters: FiltersDTO,
        direction: String = FORWARD
    ) {
        viewModelScope.launch {
            try {
                if (
                    (_startOfNextDocument == null && _endOfPrevDocument == null) ||
                    (direction == FORWARD && _startOfNextDocument != null) ||
                    (direction == BACK && _endOfPrevDocument != null)
                    ) _recipesRepo.filterRecipes(
                        filters = filters,
                        endOfPrevDocument = if (direction == BACK) _endOfPrevDocument else null,
                        startOfNextDocument = if (direction == FORWARD) _startOfNextDocument else null
                    ).collect { response ->
                        Log.d("Taag", response.toString())
                        when (response) {
                            is Response.Loading -> _loading.value = true
                            is Response.Success -> {
                                if (response.data.recipes.isEmpty()) {
                                    if (direction == FORWARD)
                                        _startOfNextDocument = null
                                    else
                                        _endOfPrevDocument = null
                                }
                                else {
                                    _recipesList.value = response.data.recipes
                                    _startOfNextDocument = response.data.startOfNextDocument
                                    _endOfPrevDocument = response.data.endOfPrevDocument
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

    companion object {
        private const val FORWARD = "forward"
        private const val BACK = "back"
    }
}