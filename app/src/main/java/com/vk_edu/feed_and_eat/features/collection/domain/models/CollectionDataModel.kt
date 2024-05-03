package com.vk_edu.feed_and_eat.features.collection.domain.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Compilation(
    val id: String? = null,
    val name : String = "",
    val picture : String? = null,
)
