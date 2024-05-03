package com.vk_edu.feed_and_eat.features.collection.domain.models

import com.google.firebase.firestore.IgnoreExtraProperties
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard

@IgnoreExtraProperties
data class Compilation(
    val id: String? = null,
    val name : String = "",
    val recipes : MutableList<RecipeCard> = mutableListOf(),
    val picture : String? = null,
)
