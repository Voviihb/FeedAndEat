package com.vk_edu.feed_and_eat.features.dishes.domain.models

data class PaginationResult(
    val recipes: List<Recipe>,
    val currentOffset: Int = 0,
    val hasMore: Boolean = false,
    val totalCount: Int? = null
)
