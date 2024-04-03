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
    val timers: ArrayList<Timer>? = null
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
    val instructions: ArrayList<Instruction>? = null,
    val servings: Servings? = null,
    val ingredients: ArrayList<Ingredient>? = null,
    val tags: ArrayList<String>? = null,
    val nutrients: Nutrients? = null,
    val author: Int = 0
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

