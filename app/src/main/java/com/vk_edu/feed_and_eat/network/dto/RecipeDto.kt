package com.vk_edu.feed_and_eat.network.dto

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class InstructionDto(
    val paragraph: String,
    val timers: List<TimerDto>? = null
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TimerDto(
    val type: String = "",
    val lowerLimit: Int? = null,
    val upperLimit: Int? = null,
    val number: Int? = null,
    val id: String? = null
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class IngredientDto(
    val name: String,
    val amount: Double,
    val unit: String
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class NutrientsDto(
    @SerialName("Calories") val calories: Double? = null,
    @SerialName("Sugar") val sugar: Double? = null,
    @SerialName("Protein") val protein: Double? = null,
    @SerialName("Fat") val fat: Double? = null,
    @SerialName("Carbohydrates") val carbohydrates: Double? = null
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ServingsDto(
    val amount: Int? = null,
    val weight: Int? = null
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ReviewDto(
    val id: String,
    @SerialName("recipe_id") val recipeId: String,
    @SerialName("user_id") val userId: String,
    val mark: Double,
    @SerialName("created_at") val createdAt: String
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ReviewCreateDto(
    val mark: Double
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class RecipeDto(
    val id: String,
    @SerialName("user_id") val userId: String,
    val name: String,
    @SerialName("image_url") val imageUrl: String? = null,
    val instructions: List<InstructionDto>,
    val servings: ServingsDto? = null,
    val ingredients: List<IngredientDto>? = null,
    val tags: List<String>? = null,
    val nutrients: NutrientsDto? = null,
    val rating: Double = 0.0,
    val cooked: Int = 0,
    @SerialName("created_at") val createdAt: String
)

