package com.vk_edu.feed_and_eat.features.dishes.domain.models

import java.util.Date
import java.util.UUID

data class Ingredient(
    val name: String = "",
    val amount: Double = 0.0,
    val unit: String = ""
)

data class Instruction(
    val paragraph: String = "",
    val timers: List<Timer>? = null
)

data class Nutrients(
    val calories: Double? = null,
    val sugar: Double? = null,
    val protein: Double? = null,
    val fat: Double? = null,
    val carbohydrates: Double? = null
)

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
    val user: String? = null,
    val rating: Double = 0.0,
    val cooked: Int = 0,
    val reviews: List<Review>? = null,
    val created: Date? = null
)

data class Servings(
    val amount: Int? = null,
    val weight: Int? = null
)

data class Timer(
    val type: String = "",
    val lowerLimit: Int? = null,
    val upperLimit: Int? = null,
    val number: Int? = null,
    val id: String = UUID.randomUUID().toString()
)

data class Review(
    val author: String = "",
    val mark: Double = 0.0
)


fun serializeNutrients(nutrients: Nutrients): Map<String, Any?> {
    return mapOf(
        "Calories" to nutrients.calories,
        "Sugar" to nutrients.sugar,
        "Protein" to nutrients.protein,
        "Fat" to nutrients.fat,
        "Carbohydrates" to nutrients.carbohydrates
    )
}
