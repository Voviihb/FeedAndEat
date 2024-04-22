package com.vk_edu.feed_and_eat.features.main.domain.models

data class CardDataModel (
    val link: String = "",
    val ingredients: Int = 0,
    val steps: Int = 0,
    val name: String = "",
    val rating: Double = 0.0,
    val cooked: Int = 0
)