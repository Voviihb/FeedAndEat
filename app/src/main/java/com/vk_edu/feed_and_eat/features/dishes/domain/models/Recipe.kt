package com.vk_edu.feed_and_eat.features.dishes.domain.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Ingredient(
    val name: String = "",
    val amount: Double = 0.0,
    val unit: String = ""
)

@IgnoreExtraProperties
data class Instruction(
    val paragraph: String = "",
    val timers: List<Timer>? = null
)

/*TODO Rename to lowercase in repo*/
@IgnoreExtraProperties
data class Nutrients(
    val Calories: Double? = null,
    val Sugar: Double? = null,
    val Protein: Double? = null,
    val Fat: Double? = null,
    val Carbohydrates: Double? = null
)

@IgnoreExtraProperties
data class Recipe(
    val id: String? = null,
    val name: String = "",
    val image: String? = null,
    val instructions: List<Instruction> = listOf(),
    val servings: Servings? = null,
    val ingredients: List<Ingredient> = listOf(),
    val tags: List<String>? = null,
    val nutrients: Nutrients = Nutrients(),
    val author: Int = 0,
    val rating: Double = 0.0,
    val cooked: Int = 0,
    val reviews: List<Review>? = null
)

@IgnoreExtraProperties
data class Servings(
    val amount: Int? = null,
    val weight: Int? = null
)

@IgnoreExtraProperties
data class Timer(
    val type: String = "",
    val lowerLimit: Int? = null,
    val upperLimit: Int? = null,
    val number: Int? = null
)

@IgnoreExtraProperties
data class Review(
    val author: String = "",
    val mark: Double = 0.0,
    val message: String? = null
)
