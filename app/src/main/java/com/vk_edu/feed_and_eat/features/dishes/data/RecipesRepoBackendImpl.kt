package com.vk_edu.feed_and_eat.features.dishes.data

import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
import com.vk_edu.feed_and_eat.features.dishes.domain.models.CollectionRecipes
import com.vk_edu.feed_and_eat.features.dishes.domain.models.PaginationResult
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Review
import com.vk_edu.feed_and_eat.features.dishes.domain.models.SearchFilters
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Tag
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Type
import com.vk_edu.feed_and_eat.features.dishes.domain.repository.RecipesRepository
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.network.api.CollectionsApi
import com.vk_edu.feed_and_eat.features.network.api.TagsApi
import com.vk_edu.feed_and_eat.local.CollectionSyncManager
import com.vk_edu.feed_and_eat.local.LocalCollectionsDataSource
import com.vk_edu.feed_and_eat.local.LocalRecipeMapper
import com.vk_edu.feed_and_eat.local.NetworkMonitor
import com.vk_edu.feed_and_eat.network.api.RecipesApi
import com.vk_edu.feed_and_eat.network.dto.ReviewCreateDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesRepoBackendImpl @Inject constructor(
    private val recipesApi: RecipesApi,
    private val collectionsApi: CollectionsApi,
    private val tagsApi: TagsApi,
    private val localDataSource: LocalCollectionsDataSource,
    private val syncManager: CollectionSyncManager,
    private val networkMonitor: NetworkMonitor,
) : RecipesRepository {

    override fun loadRecipeById(id: String): Flow<Response<Recipe?>> = repoTryCatchBlock {
        var remoteError: Exception? = null

        if (networkMonitor.isOnlineNow()) {
            try {
                syncManager.syncPendingOperations()
                val dto = recipesApi.getRecipe(id)
                localDataSource.upsertRecipe(dto)
                return@repoTryCatchBlock LocalRecipeMapper.dtoToDomain(dto)
            } catch (e: Exception) {
                remoteError = e
            }
        }

        localDataSource.getRecipeById(id)
            ?: throw (remoteError ?: IllegalStateException("Recipe is not available offline"))
    }.flowOn(Dispatchers.IO)

    override fun loadDailyRecipe(): Flow<Response<Recipe?>> = repoTryCatchBlock {
        val dto = recipesApi.getDailyRecipe()
        LocalRecipeMapper.dtoToDomain(dto)
    }.flowOn(Dispatchers.IO)

    override fun loadTopRatingRecipes(): Flow<Response<List<Recipe>>> = repoTryCatchBlock {
        val recipes = recipesApi.getTopRecipes(limit = 10)
        recipes.map { LocalRecipeMapper.dtoToDomain(it) }
    }.flowOn(Dispatchers.IO)

    override fun loadLowCalorieRecipes(): Flow<Response<List<Recipe>>> = repoTryCatchBlock {
        val recipes = recipesApi.getLowCalorieRecipes(maxCalories = 300.0, limit = 10)
        recipes.map { LocalRecipeMapper.dtoToDomain(it) }
    }.flowOn(Dispatchers.IO)

    override fun loadLastAddedRecipes(): Flow<Response<List<Recipe>>> = repoTryCatchBlock {
        val recipes = recipesApi.getLatestRecipes(limit = 10)
        recipes.map { LocalRecipeMapper.dtoToDomain(it) }
    }.flowOn(Dispatchers.IO)

    override fun loadBreakfastRecipes(): Flow<Response<List<Recipe>>> = repoTryCatchBlock {
        val recipes = recipesApi.searchRecipes(tags = listOf("завтрак"), limit = 10)
        recipes.map { LocalRecipeMapper.dtoToDomain(it) }
    }.flowOn(Dispatchers.IO)

    override fun loadSearchRecipes(
        filters: SearchFilters,
        type: Type?,
        offset: Int,
        limit: Int,
    ): Flow<Response<PaginationResult>> = repoTryCatchBlock {
        val recipes = recipesApi.searchRecipes(
            query = filters.startsWith.takeIf { it.isNotBlank() },
            caloriesMin = filters.caloriesMin.takeIf { it > 0.0 },
            caloriesMax = filters.caloriesMax.takeIf { it < 10e9 },
            proteinMin = filters.proteinMin.takeIf { it > 0.0 },
            proteinMax = filters.proteinMax.takeIf { it < 10e9 },
            fatMin = filters.fatMin.takeIf { it > 0.0 },
            fatMax = filters.fatMax.takeIf { it < 10e9 },
            carbsMin = filters.carbohydratesMin.takeIf { it > 0.0 },
            carbsMax = filters.carbohydratesMax.takeIf { it < 10e9 },
            sugarMin = filters.sugarMin.takeIf { it > 0.0 },
            sugarMax = filters.sugarMax.takeIf { it < 10e9 },
            tags = filters.tags.takeIf { it.isNotEmpty() },
            sort = when (filters.sort) {
                1 -> "rating"
                2 -> "popularity"
                else -> "new"
            },
            limit = limit + 1,
            offset = offset,
        )

        val hasMore = recipes.size > limit
        val actualRecipes = if (hasMore) recipes.take(limit) else recipes

        PaginationResult(
            recipes = actualRecipes.map { LocalRecipeMapper.dtoToDomain(it) },
            currentOffset = offset,
            hasMore = hasMore,
        )
    }.flowOn(Dispatchers.IO)

    override fun loadTags(): Flow<Response<List<Tag>>> = repoTryCatchBlock {
        tagsApi.getTags().map { Tag(name = it.name) }
    }.flowOn(Dispatchers.IO)

    override fun loadCollectionRecipesId(id: String): Flow<Response<CollectionRecipes?>> = repoTryCatchBlock {
        if (networkMonitor.isOnlineNow()) {
            try {
                syncManager.syncAllIfOnline()
            } catch (_: Exception) {
            }
        }

        val localIds = localDataSource.getCollectionRecipeIds(id)
        if (localIds.isNotEmpty()) {
            return@repoTryCatchBlock CollectionRecipes(recipeIds = localIds)
        }

        val collectionDto = collectionsApi.getCollection(id)
        val recipes = collectionsApi.getCollectionRecipes(id)
        localDataSource.replaceCollectionRecipes(id, recipes)
        CollectionRecipes(recipeIds = collectionDto.recipeIds)
    }.flowOn(Dispatchers.IO)

    override fun loadCollectionRecipesCards(id: String): Flow<Response<List<Recipe>?>> = repoTryCatchBlock {
        if (networkMonitor.isOnlineNow()) {
            try {
                syncManager.syncAllIfOnline()
            } catch (_: Exception) {
            }
        }

        val localRecipes = localDataSource.getCollectionRecipes(id)
        if (localRecipes.isNotEmpty()) {
            return@repoTryCatchBlock localRecipes
        }

        val remote = collectionsApi.getCollectionRecipes(id)
        localDataSource.replaceCollectionRecipes(id, remote)
        remote.map { LocalRecipeMapper.dtoToDomain(it) }
    }.flowOn(Dispatchers.IO)

    override fun addRecipeToUserCollection(
        collectionId: String,
        recipeId: String,
        image: String?,
    ): Flow<Response<Unit>> = repoTryCatchBlock {
        localDataSource.addRecipeRef(collectionId, recipeId)
        localDataSource.enqueueOperation(collectionId, recipeId, CollectionSyncManager.OP_ADD)

        if (networkMonitor.isOnlineNow()) {
            runCatching {
                val dto = recipesApi.getRecipe(recipeId)
                localDataSource.upsertRecipe(dto)
            }
            syncManager.syncAllIfOnline()
        }
    }.flowOn(Dispatchers.IO)

    override fun removeRecipeFromUserCollection(
        collectionId: String,
        recipeId: String,
    ): Flow<Response<Unit>> = repoTryCatchBlock {
        localDataSource.removeRecipeRef(collectionId, recipeId)
        localDataSource.enqueueOperation(collectionId, recipeId, CollectionSyncManager.OP_REMOVE)

        if (networkMonitor.isOnlineNow()) {
            syncManager.syncAllIfOnline()
        }
    }.flowOn(Dispatchers.IO)

    override fun loadMyReviewOnRecipe(id: String): Flow<Response<Review?>> = repoTryCatchBlock {
        val reviewDto = try {
            recipesApi.getMyReview(id)
        } catch (e: Exception) {
            null
        }
        reviewDto?.let { Review(author = it.userId, mark = it.mark) }
    }.flowOn(Dispatchers.IO)

    override fun addNewReviewOnRecipe(id: String, review: Review): Flow<Response<Unit>> = repoTryCatchBlock {
        if (networkMonitor.isOnlineNow()) {
            try {
                recipesApi.addReview(id, ReviewCreateDto(mark = review.mark))
            } catch (e: HttpException) {
                if (e.code() == 409) {
                    recipesApi.updateMyReview(id, ReviewCreateDto(mark = review.mark))
                } else {
                    throw e
                }
            }
        } else {
            localDataSource.enqueueRecipeReview(id, review.mark)
        }

        if (networkMonitor.isOnlineNow()) {
            syncManager.syncPendingRecipeOperations()
        }
        Unit
    }.flowOn(Dispatchers.IO)

    override fun updateReviewOnRecipe(
        id: String,
        oldReview: Review,
        newReview: Review,
    ): Flow<Response<Unit>> = repoTryCatchBlock {
        if (networkMonitor.isOnlineNow()) {
            recipesApi.updateMyReview(id, ReviewCreateDto(mark = newReview.mark))
        } else {
            localDataSource.enqueueRecipeReview(id, newReview.mark)
        }

        if (networkMonitor.isOnlineNow()) {
            syncManager.syncPendingRecipeOperations()
        }
        Unit
    }.flowOn(Dispatchers.IO)

    override fun incrementCookedCounter(id: String): Flow<Response<Unit>> = repoTryCatchBlock {
        if (networkMonitor.isOnlineNow()) {
            recipesApi.incrementCookedCounter(id)
        } else {
            localDataSource.enqueueCookedIncrement(id)
        }

        if (networkMonitor.isOnlineNow()) {
            syncManager.syncPendingRecipeOperations()
        }
    }.flowOn(Dispatchers.IO)
}
