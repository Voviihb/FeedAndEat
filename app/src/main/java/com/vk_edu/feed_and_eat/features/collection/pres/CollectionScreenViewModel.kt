package com.vk_edu.feed_and_eat.features.collection.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.collection.domain.models.Compilation
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
    private val id = "xJwRsIERXsZx3GtCxXch"

    private val _cardsData = mutableStateOf(listOf<RecipeCard>())
    var cardsData: State<List<RecipeCard>> = _cardsData

    private val _collectionsData = mutableStateOf(listOf<Compilation>())

    private val _favouritesData = mutableStateOf(listOf<String>())
    val favouritesData: State<List<String>> = _favouritesData

    private val _favouritesId = mutableStateOf<String?>(null)
    val favouritesId: State<String?> = _favouritesId

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    init {
        loadUserFavourites()
    }

    fun collectionRecipes(/*TODO pass id of collection here*/) {
        viewModelScope.launch {
            try {
                _recipesRepo.loadCollectionRecipes(id).collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
//                            if (response.data != null)
//                                _cardsData.value = response.data.recipeCards TODO
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
                val user = _authRepo.getUserId()
                if (user != null) {
                    _usersRepo.getUserCollections(userId = user).collect { response ->
                        when (response) {
                            is Response.Loading -> _loading.value = true
                            is Response.Success -> {
                                if (response.data != null) {
                                    _collectionsData.value = response.data
                                }
                            }

                            is Response.Failure -> {
                                onError(response.e)
                            }
                        }
                    }

                    val favouritesId =
                        _collectionsData.value.filter { it.name == "Favourites" }[0].id
                    _favouritesId.value = favouritesId

                    if (favouritesId != null) {
                        _recipesRepo.loadCollectionRecipes(id = favouritesId).collect { response ->
                            when (response) {
                                is Response.Loading -> _loading.value = true
                                is Response.Success -> {
                                    if (response.data != null) {
                                        _favouritesData.value = response.data.recipeIds
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

            } catch (e: Exception) {
                onError(e)
            }
            _loading.value = false
        }
    }

    fun removeRecipeFromUserCollection(collectionId: String, recipe: RecipeCard) {
        viewModelScope.launch {
            try {
                _recipesRepo.removeRecipeFromUserCollection(collectionId, recipe.recipeId)
                    .collect { response ->
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