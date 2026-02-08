package com.vk_edu.feed_and_eat.features.search.pres

import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard

data class CardsAndSnapshots(
    val cards: List<RecipeCard>,
    val currentOffset: Int = 0,
    val hasMore: Boolean = false
)