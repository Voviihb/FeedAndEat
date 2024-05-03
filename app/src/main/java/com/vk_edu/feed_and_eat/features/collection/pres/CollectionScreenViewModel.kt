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
    private val _usersRepo: UsersRepoImpl,
    private val _authRepo: AuthRepoImpl
) : ViewModel() {
    private val id = "EsnDuK3RbBVAgHDn6hMH"

    private val _cardsData = mutableStateOf(listOf<RecipeCard>())
    var cardsData: State<List<RecipeCard>> = _cardsData

    private val _collectionsData = mutableStateOf(listOf<Compilation>())
    val collectionsData: State<List<Compilation>> = _collectionsData

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
                            if (response.data != null)
                                _cardsData.value = response.data.recipeCards
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

    fun loadAllUserCollections() {
        viewModelScope.launch {
            try {
                val user = _authRepo.getUserId()
                if (user != null) {
                    _usersRepo.getUserCollections(userId = user).collect { response ->
                        when (response) {
                            is Response.Loading -> _loading.value = true
                            is Response.Success -> {
                                if (response.data != null) {
                                    for (collection in response.data) {
                                        _collectionsData.value += collection
                                    }
                                }
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

    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}