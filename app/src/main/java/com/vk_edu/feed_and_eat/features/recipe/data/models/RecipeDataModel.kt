package com.vk_edu.feed_and_eat.features.recipe.data.models

data class RecipeDataModel(
    val picture: Int = -1,
    val rating: Double = -1.0,
    val cooked: Int = -1,
    val description: List<String> = listOf(""),
    val name: String = "",
    val ingredients: List<String> = listOf(""),
    val steps: List<String> = listOf(""),
    val tags: List<String> = listOf(""),
    val energyData: List<Int> = listOf(-1),
    val pictureHeight: Int = -1,
)