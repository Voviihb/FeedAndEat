package com.vk_edu.feed_and_eat.features.recipe.pres.preview

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.dishes.data.RecipesRepoImpl
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoImpl
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.profile.data.UsersRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesScreenViewModel @Inject constructor(
    private val _authRepo: AuthRepoImpl,
    private val _usersRepo: UsersRepoImpl,
    private val _recipesRepo: RecipesRepoImpl
) : ViewModel() {
    private val privateRecipe = mutableStateOf(Recipe())
    var recipe : State<Recipe> = privateRecipe

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _favouritesCollectionId = mutableStateOf<String?>(null)
    val favouritesCollectionId: State<String?> = _favouritesCollectionId

    private val _favouriteRecipeIds = mutableStateOf(listOf<String>())
    val favouriteRecipeIds: State<List<String>> = _favouriteRecipeIds

    private val _collectionLoading = mutableStateOf(false)
    val collectionLoading : State<Boolean> = _collectionLoading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    private val _collectionErrorMessage = mutableStateOf<Exception?>(null)
    val collectionErrorMessage: State<Exception?> = _errorMessage

    private val _collectionList = mutableStateOf(listOf(CollectionDataModel()))
    val collectionsList : State<List<CollectionDataModel>>? = _collectionList

    private val _collectionButtonExpanded = mutableStateOf(false)
    val collectionButtonExpanded : State<Boolean> = _collectionButtonExpanded

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

    fun clearCollectionError() {
        _errorMessage.value = null
    }

    private fun onCollectionError(message: Exception?) {
        _collectionErrorMessage.value = message
        _collectionLoading.value = false
    }

    fun clearError() {
        _collectionErrorMessage.value = null
    }

    fun isUserAuthenticated() = _authRepo.isUserAuthenticatedInFirebase()

    fun loadCollections(){
        viewModelScope.launch {
            try {
                val userId = _authRepo.getUserId()
                if (userId != null){
                    _usersRepo.getUserCollections(userId).collect { response ->
                        when (response) {
                            is Response.Loading -> _collectionLoading.value = true
                            is Response.Success -> {
                                if (response.data != null) {
                                    _collectionList.value = response.data
                                }
                            }

                            is Response.Failure -> {
                                onCollectionError(response.e)
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                onCollectionError(e)
            }
            _collectionLoading.value = false
        }
    }

    fun expand(){
        _collectionButtonExpanded.value = !_collectionButtonExpanded.value
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
    fun getFavouriteRecipeIds() {
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

                    val favouritesId = collectionsData.filter {
                        it.name == "Favourites"
                    }[0].id
                    _favouritesCollectionId.value = favouritesId

                    if (favouritesId != null) {
                        _recipesRepo.loadCollectionRecipesId(id = favouritesId).collect { response ->
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

    fun inFavourite(): Boolean {
        return recipe.value.id in favouriteRecipeIds.value
    }


    init {
        getFavouriteRecipeIds()
    }
}