package com.vk_edu.feed_and_eat.features.search.pres

import com.google.firebase.firestore.DocumentSnapshot
import com.vk_edu.feed_and_eat.features.search.domain.models.CardDataModel

data class CardsAndSnapshots(
    val cards: List<CardDataModel>,
    val firstDocument: DocumentSnapshot?,
    val lastDocument: DocumentSnapshot?
)