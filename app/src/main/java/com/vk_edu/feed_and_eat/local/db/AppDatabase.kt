package com.vk_edu.feed_and_eat.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vk_edu.feed_and_eat.local.db.dao.OfflineCollectionsDao
import com.vk_edu.feed_and_eat.local.db.entity.CachedCollectionEntity
import com.vk_edu.feed_and_eat.local.db.entity.CachedRecipeEntity
import com.vk_edu.feed_and_eat.local.db.entity.CollectionRecipeCrossRef
import com.vk_edu.feed_and_eat.local.db.entity.PendingCollectionOperationEntity
import com.vk_edu.feed_and_eat.local.db.entity.PendingRecipeOperationEntity

@Database(
    entities = [
        CachedRecipeEntity::class,
        CachedCollectionEntity::class,
        CollectionRecipeCrossRef::class,
        PendingCollectionOperationEntity::class,
        PendingRecipeOperationEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun offlineCollectionsDao(): OfflineCollectionsDao
}
