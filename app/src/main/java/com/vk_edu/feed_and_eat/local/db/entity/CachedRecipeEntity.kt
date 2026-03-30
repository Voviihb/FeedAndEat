package com.vk_edu.feed_and_eat.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_recipes")
data class CachedRecipeEntity(
    @PrimaryKey val id: String,
    val name: String,
    val imageUrl: String?,
    val rating: Double,
    val cooked: Int,
    val payloadJson: String,
    val updatedAt: Long,
)
