package com.vk_edu.feed_and_eat.features.dishes.domain.models

import com.google.firebase.firestore.DocumentSnapshot

data class PaginationResult(
    val recipes: List<Recipe>,
    val endOfPrevDocument: DocumentSnapshot?,
    val startOfNextDocument: DocumentSnapshot?
)
