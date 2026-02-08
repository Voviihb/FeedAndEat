package com.vk_edu.feed_and_eat.features.network.dto

import android.annotation.SuppressLint
import com.vk_edu.feed_and_eat.network.dto.IngredientDto
import com.vk_edu.feed_and_eat.network.dto.InstructionDto
import com.vk_edu.feed_and_eat.network.dto.NutrientsDto
import com.vk_edu.feed_and_eat.network.dto.ServingsDto
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class CreateRecipeDto(
    val name: String,
    val image_url: String? = null,
    val instructions: List<InstructionDto>,
    val servings: ServingsDto? = null,
    val ingredients: List<IngredientDto>? = null,
    val tags: List<String>? = null,
    val nutrients: NutrientsDto? = null
)