package com.vk_edu.feed_and_eat.features.collection.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.dishes.data.RecipesRepoImpl
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoImpl
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.profile.data.UsersRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CollectionScreenViewModel @Inject constructor(
    private val _recipesRepo: RecipesRepoImpl,
    private val _authRepo: AuthRepoImpl,
    private val _usersRepo: UsersRepoImpl
) : ViewModel() {
    private val _cardsData = mutableStateOf(listOf<RecipeCard>())
    var cardsData: State<List<RecipeCard>> = _cardsData

    private val _favouriteRecipeIds = mutableStateOf(listOf<String>())
    val favouriteRecipeIds: State<List<String>> = _favouriteRecipeIds

    private val _favouritesCollectionId = mutableStateOf<String?>(null)
    val favouritesCollectionId: State<String?> = _favouritesCollectionId

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    init {
        loadUserFavourites()
    }

    fun collectionRecipes(id : String) {
        viewModelScope.launch {
            try {
                _recipesRepo.loadCollectionRecipesCards(id).collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            if (response.data != null) {
                                val recipeCards = response.data.map {
                                    RecipeCard(
                                        recipeId = it.id ?: "",
                                        image = it.image ?: "",
                                        ingredients = it.ingredients.size,
                                        steps = it.instructions.size,
                                        name = it.name,
                                        rating = it.rating,
                                        cooked = it.cooked
                                    )
                                }
                                _cardsData.value = recipeCards
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

    fun loadUserFavourites() {
        viewModelScope.launch {
            try {
                var collectionsData = listOf<CollectionDataModel>()
                val user = _authRepo.getUserId()
                if (user != null) {
                    _usersRepo.getUserCollections(userId = user).collect { response ->
                        when (response) {
                            is Response.Loading -> _loading.value = true
                            is Response.Success -> {
                                if (response.data != null) {
                                    collectionsData = response.data
                                }
                            }

                            is Response.Failure -> {
                                onError(response.e)
                            }
                        }
                    }

                    val favouritesId =
                        collectionsData.filter { it.name == "Favourites" }[0].id
                    _favouritesCollectionId.value = favouritesId

                    if (favouritesId != null) {
                        _recipesRepo.loadCollectionRecipesId(id = favouritesId)
                            .collect { response ->
                                when (response) {
                                    is Response.Loading -> _loading.value = true
                                    is Response.Success -> {
                                        if (response.data != null) {
                                            _favouriteRecipeIds.value = response.data.recipeIds
                                        }
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

    fun addRecipeToUserCollection(collectionId: String, recipe: RecipeCard) {
        viewModelScope.launch {
            try {
                val user = _authRepo.getUserId()
                if (user != null) {
                    _recipesRepo.addRecipeToUserCollection(
                        user,
                        collectionId,
                        recipe.recipeId,
                        recipe.image
                    ).collect { response ->
                        when (response) {
                            is Response.Loading -> { }
                            is Response.Success -> {
                                val favouriteIds = _favouriteRecipeIds.value.toMutableList()
                                favouriteIds.add(recipe.recipeId)
                                _favouriteRecipeIds.value = favouriteIds
                            }

                            is Response.Failure -> {
                                onError(response.e)
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun removeRecipeFromUserCollection(collectionId: String, recipe: RecipeCard) {
        viewModelScope.launch {
            try {
                _recipesRepo.removeRecipeFromUserCollection(collectionId, recipe.recipeId)
                    .collect { response ->
                        when (response) {
                            is Response.Loading -> { }
                            is Response.Success -> {
                                val favouriteIds = _favouriteRecipeIds.value.toMutableList()
                                favouriteIds.remove(recipe.recipeId)
                                _favouriteRecipeIds.value = favouriteIds
                            }

                            is Response.Failure -> {
                                onError(response.e)
                            }
                        }
                    }
            } catch (e: Exception) {
                onError(e)
            }
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