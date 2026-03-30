package com.vk_edu.feed_and_eat.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vk_edu.feed_and_eat.local.db.entity.CachedCollectionEntity
import com.vk_edu.feed_and_eat.local.db.entity.CachedRecipeEntity
import com.vk_edu.feed_and_eat.local.db.entity.CollectionRecipeCrossRef
import com.vk_edu.feed_and_eat.local.db.entity.PendingCollectionOperationEntity
import com.vk_edu.feed_and_eat.local.db.entity.PendingRecipeOperationEntity

@Dao
interface OfflineCollectionsDao {

    @Query("SELECT * FROM cached_collections ORDER BY name COLLATE NOCASE ASC")
    suspend fun getCollections(): List<CachedCollectionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCollections(items: List<CachedCollectionEntity>)

    @Query("DELETE FROM cached_collections")
    suspend fun clearCollections()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecipes(items: List<CachedRecipeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecipe(item: CachedRecipeEntity)

    @Query("SELECT * FROM cached_recipes WHERE id = :recipeId LIMIT 1")
    suspend fun getRecipeById(recipeId: String): CachedRecipeEntity?

    @Query("SELECT * FROM cached_recipes WHERE id IN (:ids)")
    suspend fun getRecipesByIds(ids: List<String>): List<CachedRecipeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCollectionRecipeRefs(refs: List<CollectionRecipeCrossRef>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCollectionRecipeRef(ref: CollectionRecipeCrossRef)

    @Query("DELETE FROM collection_recipe_refs WHERE collectionId = :collectionId")
    suspend fun clearCollectionRecipeRefs(collectionId: String)

    @Query("DELETE FROM collection_recipe_refs WHERE collectionId = :collectionId AND recipeId = :recipeId")
    suspend fun deleteCollectionRecipeRef(collectionId: String, recipeId: String)

    @Query("SELECT * FROM collection_recipe_refs WHERE collectionId = :collectionId ORDER BY position ASC")
    suspend fun getCollectionRecipeRefs(collectionId: String): List<CollectionRecipeCrossRef>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPendingOperation(op: PendingCollectionOperationEntity)

    @Query("SELECT * FROM pending_collection_operations ORDER BY createdAt ASC")
    suspend fun getPendingOperations(): List<PendingCollectionOperationEntity>

    @Query("DELETE FROM pending_collection_operations WHERE id = :opId")
    suspend fun deletePendingOperation(opId: Long)

    @Query("DELETE FROM pending_collection_operations WHERE collectionId = :collectionId AND recipeId = :recipeId")
    suspend fun deletePendingOperationsForPair(collectionId: String, recipeId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPendingRecipeOperation(op: PendingRecipeOperationEntity)

    @Query("SELECT * FROM pending_recipe_operations ORDER BY createdAt ASC")
    suspend fun getPendingRecipeOperations(): List<PendingRecipeOperationEntity>

    @Query("DELETE FROM pending_recipe_operations WHERE id = :opId")
    suspend fun deletePendingRecipeOperation(opId: Long)

    @Query("DELETE FROM pending_recipe_operations WHERE recipeId = :recipeId AND operationType = :operationType")
    suspend fun deletePendingRecipeOperationsByType(recipeId: String, operationType: String)
}
