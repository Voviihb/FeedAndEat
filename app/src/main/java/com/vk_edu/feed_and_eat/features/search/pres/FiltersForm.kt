package com.vk_edu.feed_and_eat.features.search.pres

data class FiltersForm(
    val tags: List<TagChecking> = listOf(),
    val nutrients: List<NutrientRange> = listOf(
        NutrientRange("", ""),
        NutrientRange("", ""),
        NutrientRange("", ""),
        NutrientRange("", ""),
        NutrientRange("", "")
    )
)
