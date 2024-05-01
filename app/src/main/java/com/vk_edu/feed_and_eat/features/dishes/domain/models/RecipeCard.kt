package com.vk_edu.feed_and_eat.features.dishes.domain.models

data class RecipeCard (
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val ingredients: Int = 0,
    val steps: Int = 0,
    val rating: Double = 0.0,
    val cooked: Int = 0
)