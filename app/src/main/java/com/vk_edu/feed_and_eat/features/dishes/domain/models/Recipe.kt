package com.vk_edu.feed_and_eat.features.dishes.domain.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Ingredient(
    val name: String? = null,
    val amount: Double? = null,
    val unit: String? = null
)

@IgnoreExtraProperties
data class Instruction(
    val paragraph: String? = null,
    val timers: List<Timer>? = null
)

@IgnoreExtraProperties
data class Nutrients(
    val calories: Double? = null,
    val sugar: Double? = null,
    val protein: Double? = null,
    val fat: Double? = null,
    val carbohydrates: Double? = null
)

@IgnoreExtraProperties
data class Recipe(
    val name: String? = null,
    val image: String? = null,
    val instructions: List<Instruction>? = null,
    val servings: Servings? = null,
    val ingredients: List<Ingredient>? = null,
    val tags: List<String>? = null,
    val nutrients: Nutrients? = null,
    val author: Int? = null,
    val rating: Double? = null,
    val cooked: Int? = null,
    val reviews: List<Review>? = null
)

@IgnoreExtraProperties
data class Servings(
    val amount: Int? = null,
    val weight: Int? = null
)

@IgnoreExtraProperties
data class Timer(
    val type: String? = null,
    val lowerLimit: Int? = null,
    val upperLimit: Int? = null,
    val number: Int? = null
)

@IgnoreExtraProperties
data class Review(
    val author: Int? = null,
    val mark: Double? = null,
    val message: String? = null
)

