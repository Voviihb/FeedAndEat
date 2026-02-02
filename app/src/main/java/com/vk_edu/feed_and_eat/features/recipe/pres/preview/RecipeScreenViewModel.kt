package com.vk_edu.feed_and_eat.features.recipe.pres.preview

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.dishes.domain.repository.RecipesRepository
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard
import com.vk_edu.feed_and_eat.features.login.domain.repository.AuthRepository
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.profile.domain.repository.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesScreenViewModel @Inject constructor(
    private val _authRepo: AuthRepository,
    private val _usersRepo: UsersRepository,
    private val _recipesRepo: RecipesRepository
) : ViewModel() {
    private val privateRecipe = mutableStateOf(Recipe())
    var recipe: State<Recipe> = privateRecipe

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _favouritesCollectionId = mutableStateOf<String?>(null)
    val favouritesCollectionId: State<String?> = _favouritesCollectionId

    private val _favouriteRecipeIds = mutableStateOf(listOf<String>())
    val favouriteRecipeIds: State<List<String>> = _favouriteRecipeIds

    private val _collectionLoading = mutableStateOf(false)
    val collectionLoading: State<Boolean> = _collectionLoading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    private val _collectionErrorMessage = mutableStateOf<Exception?>(null)
    val collectionErrorMessage: State<Exception?> = _errorMessage

    private val _collectionList = mutableStateOf(listOf(CollectionDataModel()))
    val collectionsList: State<List<CollectionDataModel>> = _collectionList

    private val _collectionButtonExpanded = mutableStateOf(false)
    val collectionButtonExpanded: State<Boolean> = _collectionButtonExpanded

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

    fun isUserAuthenticated() = _authRepo.isAuthorized()

    fun loadCollections() {
        viewModelScope.launch {
            try {
                android.util.Log.d("RecipeViewModel", "Loading user collections...")
                _usersRepo.getUserCollections().collect { response ->
                    when (response) {
                        is Response.Loading -> _collectionLoading.value = true
                        is Response.Success -> {
                            android.util.Log.d("RecipeViewModel", "Collections loaded: ${response.data?.size}")
                            if (response.data != null) {
                                _collectionList.value = response.data
                            }
                        }

                        is Response.Failure -> {
                            android.util.Log.e("RecipeViewModel", "Failed to load collections", response.e)
                            onCollectionError(response.e)
                        }
                    }
                }

            } catch (e: Exception) {
                android.util.Log.e("RecipeViewModel", "Exception loading collections", e)
                onCollectionError(e)
            }
            _collectionLoading.value = false
        }
    }

    fun addRecipeToUserCollection(collectionId: String, recipe: RecipeCard) {
        viewModelScope.launch {
            try {
                android.util.Log.d("RecipeViewModel", "Adding recipe ${recipe.recipeId} to collection $collectionId")
                _recipesRepo.addRecipeToUserCollection(
                    collectionId,
                    recipe.recipeId,
                    recipe.image
                ).collect { response ->
                    when (response) {
                        is Response.Loading -> {
                            android.util.Log.d("RecipeViewModel", "Adding to collection - Loading...")
                        }
                        is Response.Success -> {
                            android.util.Log.d("RecipeViewModel", "Successfully added to collection!")
                            val favouriteIds = _favouriteRecipeIds.value.toMutableList()
                            favouriteIds.add(recipe.recipeId)
                            _favouriteRecipeIds.value = favouriteIds
                        }

                        is Response.Failure -> {
                            android.util.Log.e("RecipeViewModel", "Failed to add to collection", response.e)
                            onError(response.e)
                        }
                    }
                }

            } catch (e: Exception) {
                android.util.Log.e("RecipeViewModel", "Exception adding to collection", e)
                onError(e)
            }
        }
    }


    fun expand() {
        _collectionButtonExpanded.value = !_collectionButtonExpanded.value
    }

    fun addRecipeToUserCollection(collectionId: String, id: String, image : String) {
        viewModelScope.launch {
            try {
                android.util.Log.d("RecipeViewModel", "Adding recipe $id to collection $collectionId")
                _recipesRepo.addRecipeToUserCollection(
                    collectionId,
                    id,
                    image
                ).collect { response ->
                    when (response) {
                        is Response.Loading -> {
                            android.util.Log.d("RecipeViewModel", "Adding recipe - Loading...")
                        }
                        is Response.Success -> {
                            android.util.Log.d("RecipeViewModel", "Successfully added recipe!")
                            val favouriteIds = _favouriteRecipeIds.value.toMutableList()
                            favouriteIds.add(id)
                            _favouriteRecipeIds.value = favouriteIds
                        }

                        is Response.Failure -> {
                            android.util.Log.e("RecipeViewModel", "Failed to add recipe", response.e)
                            onError(response.e)
                        }
                    }
                }

            } catch (e: Exception) {
                android.util.Log.e("RecipeViewModel", "Exception adding recipe", e)
                onError(e)
            }
        }
    }

    fun removeRecipeFromUserCollection(collectionId: String, id : String) {
        viewModelScope.launch {
            try {
                _recipesRepo.removeRecipeFromUserCollection(collectionId, id)
                    .collect { response ->
                        when (response) {
                            is Response.Loading -> { }
                            is Response.Success -> {
                                val favouriteIds = _favouriteRecipeIds.value.toMutableList()
                                favouriteIds.remove(id)
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
                android.util.Log.d("RecipeViewModel", "Loading favourites...")
                _usersRepo.getUserCollections().collect { response ->
                    when (response) {
                        is Response.Loading -> _loading.value = true
                        is Response.Success -> {
                            if (response.data != null) {
                                collectionsData = response.data
                                android.util.Log.d("RecipeViewModel", "Got collections: ${response.data.map { it.name }}")
                            }
                        }

                        is Response.Failure -> {
                            android.util.Log.e("RecipeViewModel", "Failed to get user collections", response.e)
                            onError(response.e)
                        }
                    }
                }

                val favouritesCollection = collectionsData.find { it.name == "Избранное" }
                val favouritesId = favouritesCollection?.id
                _favouritesCollectionId.value = favouritesId
                
                android.util.Log.d("RecipeViewModel", "Found favourites collection: $favouritesId")

                if (favouritesId != null) {
                    _recipesRepo.loadCollectionRecipesId(id = favouritesId).collect { response ->
                        when (response) {
                            is Response.Loading -> _loading.value = true
                            is Response.Success -> {
                                if (response.data != null) {
                                    _favouriteRecipeIds.value = response.data.recipeIds
                                    android.util.Log.d("RecipeViewModel", "Loaded favourite recipe IDs: ${response.data.recipeIds}")
                                }
                            }

                            is Response.Failure -> {
                                android.util.Log.e("RecipeViewModel", "Failed to load favourite recipe IDs", response.e)
                                onError(response.e)
                            }
                        }
                    }
                } else {
                    android.util.Log.w("RecipeViewModel", "No Favourites collection found!")
                }
            } catch (e: Exception) {
                android.util.Log.e("RecipeViewModel", "Exception in getFavouriteRecipeIds", e)
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