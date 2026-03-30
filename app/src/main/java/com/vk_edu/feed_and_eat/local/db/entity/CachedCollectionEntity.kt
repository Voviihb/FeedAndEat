package com.vk_edu.feed_and_eat.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_collections")
data class CachedCollectionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val pictureUrl: String?,
    val updatedAt: Long,
)
