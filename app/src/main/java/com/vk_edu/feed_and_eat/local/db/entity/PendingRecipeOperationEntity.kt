package com.vk_edu.feed_and_eat.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_recipe_operations")
data class PendingRecipeOperationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recipeId: String,
    val operationType: String,
    val reviewMark: Double? = null,
    val createdAt: Long,
)

