package com.vk_edu.feed_and_eat.features.recipe.pres

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk_edu.feed_and_eat.features.recipe.data.models.RecipeDataModel
import com.vk_edu.feed_and_eat.features.recipe.data.repository.RecipeRepo
import kotlinx.coroutines.launch


class RecipeScreenViewModel : ViewModel(){
    private val repo = RecipeRepo()
    private val privateRecipe = mutableStateOf(RecipeDataModel())
    var recipe : State<RecipeDataModel> = privateRecipe

    fun getRecipe() {
        viewModelScope.launch {
            privateRecipe.value = repo.getRecipe()
        }
    }
}