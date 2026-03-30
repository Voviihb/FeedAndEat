package com.vk_edu.feed_and_eat.local.db.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "collection_recipe_refs",
    primaryKeys = ["collectionId", "recipeId"],
    indices = [Index("collectionId"), Index("recipeId")],
)
data class CollectionRecipeCrossRef(
    val collectionId: String,
    val recipeId: String,
    val position: Int = 0,
)
