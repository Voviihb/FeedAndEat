package com.vk_edu.feed_and_eat.features.dishes.domain.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Dish(
    var id: String? = null,
    val name: String? = null,
    val rating: Double? = null,
    val cookedCount: Int? = null,
    val ingredientsCount: Int? = null,
    val stepsCount: Int? = null,
    val published: Timestamp? = null
)