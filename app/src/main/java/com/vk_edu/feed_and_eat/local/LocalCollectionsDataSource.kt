package com.vk_edu.feed_and_eat.local

import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.local.db.AppDatabase
import com.vk_edu.feed_and_eat.local.db.entity.CachedCollectionEntity
import com.vk_edu.feed_and_eat.local.db.entity.CollectionRecipeCrossRef
import com.vk_edu.feed_and_eat.local.db.entity.PendingCollectionOperationEntity
import com.vk_edu.feed_and_eat.local.db.entity.PendingRecipeOperationEntity
import com.vk_edu.feed_and_eat.network.dto.RecipeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalCollectionsDataSource @Inject constructor(
    private val db: AppDatabase,
) {
    private val dao = db.offlineCollectionsDao()

    suspend fun replaceCollections(collections: List<CollectionDataModel>) = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        dao.clearCollections()
        dao.upsertCollections(
            collections.filter { !it.id.isNullOrBlank() }.map {
                CachedCollectionEntity(
                    id = it.id!!,
                    name = it.name,
                    pictureUrl = it.picture,
                    updatedAt = now,
                )
            },
        )
    }

    suspend fun getCollections(): List<CollectionDataModel> = withContext(Dispatchers.IO) {
        dao.getCollections().map {
            CollectionDataModel(
                id = it.id,
                name = it.name,
                picture = it.pictureUrl,
            )
        }
    }

    suspend fun replaceCollectionRecipes(collectionId: String, recipes: List<RecipeDto>) = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        dao.upsertRecipes(recipes.map { LocalRecipeMapper.toEntity(it, now) })
        dao.clearCollectionRecipeRefs(collectionId)
        dao.upsertCollectionRecipeRefs(
            recipes.mapIndexed { index, dto ->
                CollectionRecipeCrossRef(collectionId = collectionId, recipeId = dto.id, position = index)
            },
        )
    }

    suspend fun getCollectionRecipeIds(collectionId: String): List<String> = withContext(Dispatchers.IO) {
        dao.getCollectionRecipeRefs(collectionId).map { it.recipeId }
    }

    suspend fun getCollectionRecipes(collectionId: String): List<Recipe> = withContext(Dispatchers.IO) {
        val ids = dao.getCollectionRecipeRefs(collectionId).map { it.recipeId }
        if (ids.isEmpty()) return@withContext emptyList()

        val map = dao.getRecipesByIds(ids)
            .mapNotNull { entity -> entity.id to LocalRecipeMapper.entityToDomain(entity) }
            .toMap()

        ids.mapNotNull { map[it] }
    }

    suspend fun getRecipeById(recipeId: String): Recipe? = withContext(Dispatchers.IO) {
        dao.getRecipeById(recipeId)?.let { LocalRecipeMapper.entityToDomain(it) }
    }

    suspend fun upsertRecipe(dto: RecipeDto) = withContext(Dispatchers.IO) {
        dao.upsertRecipe(LocalRecipeMapper.toEntity(dto))
    }

    suspend fun addRecipeRef(collectionId: String, recipeId: String) = withContext(Dispatchers.IO) {
        val nextPosition = dao.getCollectionRecipeRefs(collectionId).size
        dao.insertCollectionRecipeRef(
            CollectionRecipeCrossRef(
                collectionId = collectionId,
                recipeId = recipeId,
                position = nextPosition,
            ),
        )
    }

    suspend fun removeRecipeRef(collectionId: String, recipeId: String) = withContext(Dispatchers.IO) {
        dao.deleteCollectionRecipeRef(collectionId, recipeId)
    }

    suspend fun enqueueOperation(collectionId: String, recipeId: String, operationType: String) = withContext(Dispatchers.IO) {
        dao.deletePendingOperationsForPair(collectionId, recipeId)
        dao.insertPendingOperation(
            PendingCollectionOperationEntity(
                collectionId = collectionId,
                recipeId = recipeId,
                operationType = operationType,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }

    suspend fun getPendingOperations() = withContext(Dispatchers.IO) {
        dao.getPendingOperations()
    }

    suspend fun deletePendingOperation(opId: Long) = withContext(Dispatchers.IO) {
        dao.deletePendingOperation(opId)
    }

    suspend fun enqueueRecipeReview(recipeId: String, mark: Double) = withContext(Dispatchers.IO) {
        dao.deletePendingRecipeOperationsByType(recipeId, CollectionSyncManager.OP_REVIEW_UPSERT)
        dao.insertPendingRecipeOperation(
            PendingRecipeOperationEntity(
                recipeId = recipeId,
                operationType = CollectionSyncManager.OP_REVIEW_UPSERT,
                reviewMark = mark,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }

    suspend fun enqueueCookedIncrement(recipeId: String) = withContext(Dispatchers.IO) {
        dao.insertPendingRecipeOperation(
            PendingRecipeOperationEntity(
                recipeId = recipeId,
                operationType = CollectionSyncManager.OP_COOKED_INCREMENT,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }

    suspend fun getPendingRecipeOperations() = withContext(Dispatchers.IO) {
        dao.getPendingRecipeOperations()
    }

    suspend fun deletePendingRecipeOperation(opId: Long) = withContext(Dispatchers.IO) {
        dao.deletePendingRecipeOperation(opId)
    }
}
