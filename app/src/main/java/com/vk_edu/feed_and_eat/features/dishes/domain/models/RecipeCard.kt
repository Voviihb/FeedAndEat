package com.vk_edu.feed_and_eat.features.dishes.domain.models

import androidx.compose.runtime.Immutable
import com.google.firebase.firestore.IgnoreExtraProperties

@Immutable
@IgnoreExtraProperties
data class RecipeCard (
    val recipeId: String = "",
    val name: String = "",
    val image: String = "",
    val ingredients: Int = 0,
    val steps: Int = 0,
    val rating: Double = 0.0,
    val cooked: Int = 0
)