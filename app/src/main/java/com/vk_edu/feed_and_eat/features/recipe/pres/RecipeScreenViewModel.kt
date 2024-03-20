package com.vk_edu.feed_and_eat.features.recipe.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.recipe.data.models.RecipeDataModel
import com.vk_edu.feed_and_eat.features.recipe.data.repository.RecipeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeScreenViewModel @Inject constructor() : ViewModel(){
    private val repo = RecipeRepo()
    private val privateRecipe = mutableStateOf(RecipeDataModel())
    var recipe : State<RecipeDataModel> = privateRecipe

    fun getRecipe() {
        viewModelScope.launch {
            privateRecipe.value = repo.getRecipe()
        }
    }
}