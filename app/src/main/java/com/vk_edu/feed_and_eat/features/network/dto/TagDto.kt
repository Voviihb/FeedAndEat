package com.vk_edu.feed_and_eat.features.network.dto

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TagDto(
    val id: String,
    val name: String,
    val created_at: String
)