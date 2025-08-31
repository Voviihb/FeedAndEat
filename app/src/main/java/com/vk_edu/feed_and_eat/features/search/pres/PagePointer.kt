package com.vk_edu.feed_and_eat.features.search.pres

import com.google.firebase.firestore.DocumentSnapshot
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Type

data class PagePointer(
    val type: Type?,
    val number: Int,
    val documentSnapshot: DocumentSnapshot? = null, // Для совместимости с Firebase
    val offset: Int = 0                             // Для backend пагинации
)