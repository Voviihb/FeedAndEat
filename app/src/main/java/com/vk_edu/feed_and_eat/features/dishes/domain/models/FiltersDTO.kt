package com.vk_edu.feed_and_eat.features.dishes.domain.models

data class FiltersDTO(
    val sort: SortFilter,
    val limit: Long,
    val startsWith: String? = null,
    val tags: List<String>? = null,
    val caloriesMin: Double = 0.0,
    val caloriesMax: Double = 10e9,
    val sugarMin: Double = 0.0,
    val sugarMax: Double = 10e9,
    val proteinMin: Double = 0.0,
    val proteinMax: Double = 10e9,
    val fatMin: Double = 0.0,
    val fatMax: Double = 10e9,
    val carbohydratesMin: Double = 0.0,
    val carbohydratesMax: Double = 10e9
)
