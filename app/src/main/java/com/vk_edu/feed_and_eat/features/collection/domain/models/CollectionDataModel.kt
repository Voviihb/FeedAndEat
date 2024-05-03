package com.vk_edu.feed_and_eat.features.collection.domain.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard

@IgnoreExtraProperties
data class Compilation(
    val name : String = "",
    val recipes : List<RecipeCard> = listOf(),
    val picture : String? = null,
)