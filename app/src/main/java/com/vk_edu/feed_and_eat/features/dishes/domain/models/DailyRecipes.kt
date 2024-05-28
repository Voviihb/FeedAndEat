package com.vk_edu.feed_and_eat.features.dishes.domain.models

data class DailyRecipes(
    val dayOfYear: Int = 0,
    val recipesOfDay: List<String> = listOf()
)
