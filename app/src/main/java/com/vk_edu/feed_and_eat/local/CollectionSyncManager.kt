package com.vk_edu.feed_and_eat.local

import com.vk_edu.feed_and_eat.features.collection.domain.models.CollectionDataModel
import com.vk_edu.feed_and_eat.features.network.api.CollectionsApi
import com.vk_edu.feed_and_eat.network.api.RecipesApi
import com.vk_edu.feed_and_eat.network.dto.ReviewCreateDto
import retrofit2.HttpException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectionSyncManager @Inject constructor(
    private val collectionsApi: CollectionsApi,
    private val recipesApi: RecipesApi,
    private val localDataSource: LocalCollectionsDataSource,
    private val networkMonitor: NetworkMonitor,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        scope.launch {
            networkMonitor.observeOnline().collectLatest { online ->
                if (online) {
                    syncAllIfOnline()
                }
            }
        }
    }

    suspend fun syncAllIfOnline() {
        if (!networkMonitor.isOnlineNow()) return
        syncPendingOperations()
        syncPendingRecipeOperations()
        refreshCollectionsAndRecipesFromRemote()
    }

    suspend fun refreshCollectionsAndRecipesFromRemote() {
        if (!networkMonitor.isOnlineNow()) return

        val collections = collectionsApi.getMyCollections()
        val localCollections = collections.map {
            CollectionDataModel(
                id = it.id,
                name = it.name,
                picture = it.pictureUrl,
            )
        }

        localDataSource.replaceCollections(localCollections)

        for (collection in collections) {
            val fullCollection = collectionsApi.getCollection(collection.id)
            val recipes = collectionsApi.getCollectionRecipes(collection.id)
            localDataSource.replaceCollectionRecipes(fullCollection.id, recipes)
        }
    }

    suspend fun syncPendingOperations() {
        if (!networkMonitor.isOnlineNow()) return
        val pending = localDataSource.getPendingOperations()
        for (op in pending) {
            try {
                when (op.operationType) {
                    OP_ADD -> collectionsApi.addRecipeToCollection(op.collectionId, op.recipeId)
                    OP_REMOVE -> collectionsApi.removeRecipeFromCollection(op.collectionId, op.recipeId)
                }
                localDataSource.deletePendingOperation(op.id)
            } catch (_: Exception) {
                return
            }
        }
    }

    suspend fun syncPendingRecipeOperations() {
        if (!networkMonitor.isOnlineNow()) return
        val pending = localDataSource.getPendingRecipeOperations()
        for (op in pending) {
            try {
                when (op.operationType) {
                    OP_REVIEW_UPSERT -> {
                        val mark = op.reviewMark ?: 0.0
                        try {
                            recipesApi.addReview(op.recipeId, ReviewCreateDto(mark = mark))
                        } catch (e: HttpException) {
                            if (e.code() == 409) {
                                recipesApi.updateMyReview(op.recipeId, ReviewCreateDto(mark = mark))
                            } else {
                                throw e
                            }
                        }
                    }

                    OP_COOKED_INCREMENT -> {
                        recipesApi.incrementCookedCounter(op.recipeId)
                    }
                }
                localDataSource.deletePendingRecipeOperation(op.id)
            } catch (_: Exception) {
                return
            }
        }
    }

    companion object {
        const val OP_ADD = "ADD"
        const val OP_REMOVE = "REMOVE"
        const val OP_REVIEW_UPSERT = "REVIEW_UPSERT"
        const val OP_COOKED_INCREMENT = "COOKED_INCREMENT"
    }
}
