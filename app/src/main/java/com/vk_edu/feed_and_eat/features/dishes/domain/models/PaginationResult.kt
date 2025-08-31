package com.vk_edu.feed_and_eat.features.dishes.domain.models

import com.google.firebase.firestore.DocumentSnapshot

data class PaginationResult(
    val recipes: List<Recipe>,
    val startDocument: DocumentSnapshot? = null, // Для совместимости с Firebase
    val endDocument: DocumentSnapshot? = null,   // Для совместимости с Firebase
    val currentOffset: Int = 0,                  // Для backend пагинации
    val hasMore: Boolean = false,                // Есть ли еще данные
    val totalCount: Int? = null                  // Общее количество (опционально)
)
