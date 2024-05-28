package com.vk_edu.feed_and_eat.features.search.pres

import com.google.firebase.firestore.DocumentSnapshot
import com.vk_edu.feed_and_eat.features.dishes.domain.models.RecipeCard

data class CardsAndSnapshots(
    val cards: List<RecipeCard>,
    val firstDocument: DocumentSnapshot?,
    val lastDocument: DocumentSnapshot?
)