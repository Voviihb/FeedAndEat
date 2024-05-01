package com.vk_edu.feed_and_eat.features.collection.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.collection.domain.models.CardDataModel
import com.vk_edu.feed_and_eat.features.dishes.data.RecipesRepoImpl
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CollectionScreenViewModel @Inject constructor(
    private val _recipesRepo: RecipesRepoImpl
) : ViewModel() {
    private val id = "083KzNCvzuf4CIKoeJFB"

    private val _cardsData = mutableStateOf(listOf<CardDataModel>())
    var cardsData: State<List<CardDataModel>> = _cardsData

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    fun collectionRecipes() {
        viewModelScope.launch {
            try {
                _recipesRepo.loadCollectionRecipes(id).collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            _cardsData.value = response.data.map { fullRecipe ->
                                CardDataModel(
                                    link = fullRecipe.image ?: "",
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