package com.vk_edu.feed_and_eat.features.recipe.data.models

data class RecipeDataModel(
    val picture: String = "",
    val rating: Double = 0.0,
    val cooked: Int = 0,
    val description: List<String> = listOf(""),
    val name: String = "",
    val ingredients: List<String> = listOf(""),
    val steps: List<String> = listOf(""),
    val tags: List<String> = listOf(""),
    val energyData: List<Int> = listOf(-1),
    val pictureHeight: Int = -1,
)