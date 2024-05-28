package com.vk_edu.feed_and_eat.features.main.pres

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
class HomeScreenViewModel @Inject constructor(
    private val _recipesRepo: RecipesRepoImpl,
    private val _authRepo: AuthRepoImpl,
    private val _usersRepo: UsersRepoImpl
) : ViewModel() {
    private val _largeCardData = mutableStateOf(RecipeCard())
    val largeCardData: State<RecipeCard> = _largeCardData

    private val _cardsDataOfRow1 = mutableStateOf(listOf<RecipeCard>())
    val cardsDataOfRow1: State<List<RecipeCard>> = _cardsDataOfRow1

    private val _cardsDataOfRow2 = mutableStateOf(listOf<RecipeCard>())
    val cardsDataOfRow2: State<List<RecipeCard>> = _cardsDataOfRow2

    private val _cardsDataOfRow3 = mutableStateOf(listOf<RecipeCard>())
    val cardsDataOfRow3: State<List<RecipeCard>> = _cardsDataOfRow3

    private val _cardsDataOfRow4 = mutableStateOf(listOf<RecipeCard>())
    val cardsDataOfRow4: State<List<RecipeCard>> = _cardsDataOfRow4

    private val _favouriteRecipeIds = mutableStateOf(listOf<String>())
    val favouriteRecipeIds: State<List<String>> = _favouriteRecipeIds

    private val _favouritesCollectionId = mutableStateOf<String?>(null)
    val favouritesCollectionId: State<String?> = _favouritesCollectionId

    private var currentUser: String? = null

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    init {
        getFavouriteRecipeIds()
        getLargeCardData()
        getCardsDataOfRow1()
        getCardsDataOfRow2()
        getCardsDataOfRow3()
        getCardsDataOfRow4()
    }

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

    fun getFavouriteRecipeIds() {
        viewModelScope.launch {
            try {
                var collectionsData = listOf<CollectionDataModel>()
                val user = _authRepo.getUserId()
                if (user != null) {
                    currentUser = user
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
                        it.name == FAVOURITES
                    }[0].id
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
                            is Response.Loading -> {}
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
                            is Response.Loading -> {}
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

    fun checkUserChanged() {
        val user = _authRepo.getUserId()
        if (currentUser != null && currentUser != user) {
            getFavouriteRecipeIds()
        }
    }

    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }

    companion object {
        private const val FAVOURITES = "Favourites"
    }
}