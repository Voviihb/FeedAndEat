package com.vk_edu.feed_and_eat.features.recipe.domain.models

interface RecipeInfo{
    suspend fun getRecipe(): RecipeForm
}

data class RecipeForm(
    val picture: Int,
    val rating: Double,
    val cooked: Int,
    val description: List<String>,
    val name: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val tags: List<String>,
    val energyData: List<Int>,
    val pictureHeight: Int
)