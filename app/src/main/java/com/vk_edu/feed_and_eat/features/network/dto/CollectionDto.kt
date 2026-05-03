package com.vk_edu.feed_and_eat.features.network.dto

import android.annotation.SuppressLint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class CollectionDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("picture_url") val pictureUrl: String? = null,
    @SerialName("created_at") val createdAt: String,
    @SerialName("recipe_ids") val recipeIds: List<String> = emptyList()
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class CollectionBriefDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("picture_url") val pictureUrl: String? = null,
    @SerialName("created_at") val createdAt: String
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class CreateCollectionBody(
    @SerialName("name") val name: String
)