package com.vk_edu.feed_and_eat.features.new_recipe.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.vk_edu.feed_and_eat.features.dishes.data.RecipesRepoImpl
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Instruction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class NewRecipeScreenViewModel @Inject constructor(
    private val _recipesRepo: RecipesRepoImpl
) : ViewModel() {
    private val _instructions = mutableStateOf(listOf<Instruction>())
    var cardsData: State<List<Instruction>> = _instructions

    private val _name = mutableStateOf("")
    var name: State<String> = _name

    private val _imagePath = mutableStateOf("")
    var imagePath: State<String> = _imagePath

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _errorMessage = mutableStateOf<Exception?>(null)
    val errorMessage: State<Exception?> = _errorMessage

    fun collectionRecipes() {
//        viewModelScope.launch {
//            try {
//                _recipesRepo.addNewRecipe(_name, _imagePath, _instructions.value).collect { response ->
//                    when (response) {
//                        is Response.Loading -> _loading.value = true
//                        is Response.Success -> {
//
//                        }
//
//                        is Response.Failure -> {
//                            onError(response.e)
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                onError(e)
//            }
//            _loading.value = false
//        }
    }

    private fun onError(message: Exception?) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}