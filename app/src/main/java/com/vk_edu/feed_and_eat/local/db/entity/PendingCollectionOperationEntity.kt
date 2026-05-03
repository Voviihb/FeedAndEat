package com.vk_edu.feed_and_eat.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_collection_operations")
data class PendingCollectionOperationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val collectionId: String,
    val recipeId: String,
    val operationType: String,
    val createdAt: Long,
)

