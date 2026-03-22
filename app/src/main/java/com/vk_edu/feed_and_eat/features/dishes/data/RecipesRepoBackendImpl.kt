package com.vk_edu.feed_and_eat.features.dishes.data

import com.vk_edu.feed_and_eat.BuildConfig
import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
import com.vk_edu.feed_and_eat.features.dishes.domain.models.CollectionRecipes
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Ingredient
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Instruction
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Nutrients
import com.vk_edu.feed_and_eat.features.dishes.domain.models.PaginationResult
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Review
import com.vk_edu.feed_and_eat.features.dishes.domain.models.SearchFilters
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Servings
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Tag
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Timer
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Type
import com.vk_edu.feed_and_eat.features.dishes.domain.repository.RecipesRepository
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.network.api.CollectionsApi
import com.vk_edu.feed_and_eat.features.network.api.TagsApi
import com.vk_edu.feed_and_eat.network.api.RecipesApi
import com.vk_edu.feed_and_eat.network.dto.RecipeDto
import com.vk_edu.feed_and_eat.network.dto.ReviewCreateDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesRepoBackendImpl @Inject constructor(
    private val recipesApi: RecipesApi,
    private val collectionsApi: CollectionsApi,
    private val tagsApi: TagsApi,
) : RecipesRepository {

    private fun makeFullUrl(relativeUrl: String?): String? {
        if (relativeUrl == null) return null
        return if (relativeUrl.startsWith("http")) {
            relativeUrl
        } else {
            BuildConfig.API_BASE_URL.trimEnd('/') + relativeUrl
        }
    }

    private fun convertDtoToRecipe(dto: RecipeDto): Recipe {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
            val createdDate = try {
                dateFormat.parse(dto.createdAt)
            } catch (e: Exception) {
                // Fallback для формата без миллисекунд
                try {
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(dto.createdAt)
                } catch (e2: Exception) {
                    Date()
                }
            }

            return Recipe(
                id = dto.id,
                name = dto.name,
                image = makeFullUrl(dto.imageUrl),
                instructions = dto.instructions.map { instructionDto ->
                    Instruction(
                        paragraph = instructionDto.paragraph,
                        timers = instructionDto.timers?.map { timerDto ->
                            if (timerDto.type == "constant") {
                                Timer(
                                    type = timerDto.type,
                                    number = timerDto.number ?: 5,
                                    lowerLimit = null,
                                    upperLimit = null,
                                    id = timerDto.id ?: UUID.randomUUID().toString()
                                )
                            } else {
                                Timer(
                                    type = timerDto.type,
                                    number = null,
                                    lowerLimit = timerDto.lowerLimit ?: 1,
                                    upperLimit = timerDto.upperLimit ?: 5,
                                    id = timerDto.id ?: UUID.randomUUID().toString()
                                )
                            }
                        }
                    )
                },
                servings = dto.servings?.let { servingsDto ->
                    Servings(
                        amount = servingsDto.amount,
                        weight = servingsDto.weight
                    )
                },
                ingredients = dto.ingredients?.map { ingredientDto ->
                    Ingredient(
                        name = ingredientDto.name,
                        amount = ingredientDto.amount,
                        unit = ingredientDto.unit
                    )
                } ?: emptyList(),
                tags = dto.tags,
                nutrients = dto.nutrients?.let { nutrientsDto ->
                    Nutrients(
                        calories = nutrientsDto.calories,
                        sugar = nutrientsDto.sugar,
                        protein = nutrientsDto.protein,
                        fat = nutrientsDto.fat,
                        carbohydrates = nutrientsDto.carbohydrates
                    )
                } ?: Nutrients(),
                author = 0,
                user = dto.userId,
                rating = dto.rating,
                cooked = dto.cooked,
                reviews = null, // отзывы загружаются отдельно через loadMyReviewOnRecipe
                created = createdDate
            )
        } catch (e: Exception) {
            return Recipe(
                id = dto.id,
                name = dto.name,
                image = makeFullUrl(dto.imageUrl),
                rating = dto.rating,
                cooked = dto.cooked
            )
        }
    }

    override fun loadRecipeById(id: String): Flow<Response<Recipe?>> = repoTryCatchBlock {
        val dto = recipesApi.getRecipe(id)
        convertDtoToRecipe(dto)
    }.flowOn(Dispatchers.IO)

    override fun loadDailyRecipe(): Flow<Response<Recipe?>> = repoTryCatchBlock {
        try {
            val dto = recipesApi.getDailyRecipe()
            convertDtoToRecipe(dto)
        } catch (e: Exception) {
            // Если рецепт дня не установлен, возвращаем топ рецепт
            val recipes = recipesApi.getTopRecipes(limit = 1)
            if (recipes.isNotEmpty()) {
                convertDtoToRecipe(recipes.first())
            } else {
                null
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun loadTopRatingRecipes(): Flow<Response<List<Recipe>>> = repoTryCatchBlock {
        val recipes = recipesApi.getTopRecipes(limit = 10)
        recipes.map { convertDtoToRecipe(it) }
    }.flowOn(Dispatchers.IO)

    override fun loadLowCalorieRecipes(): Flow<Response<List<Recipe>>> = repoTryCatchBlock {
        val recipes = recipesApi.getLowCalorieRecipes(maxCalories = 300.0, limit = 10)
        recipes.map { convertDtoToRecipe(it) }
    }.flowOn(Dispatchers.IO)

    override fun loadLastAddedRecipes(): Flow<Response<List<Recipe>>> = repoTryCatchBlock {
        val recipes = recipesApi.getLatestRecipes(limit = 10)
        recipes.map { convertDtoToRecipe(it) }
    }.flowOn(Dispatchers.IO)

    override fun loadBreakfastRecipes(): Flow<Response<List<Recipe>>> = repoTryCatchBlock {
        val recipes = recipesApi.searchRecipes(tags = listOf("завтрак"), limit = 10)
        recipes.map { convertDtoToRecipe(it) }
    }.flowOn(Dispatchers.IO)


    override fun loadSearchRecipes(
        filters: SearchFilters,
        type: Type?,
        offset: Int,
        limit: Int
    ): Flow<Response<PaginationResult>> = repoTryCatchBlock {
        // Запрашиваем на один элемент больше, чтобы точно знать, есть ли еще данные
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
            limit = limit + 1, // Запрашиваем на 1 больше
            offset = offset
        )

        val hasMore = recipes.size > limit
        val actualRecipes = if (hasMore) recipes.take(limit) else recipes

        PaginationResult(
            recipes = actualRecipes.map { convertDtoToRecipe(it) },
            currentOffset = offset,
            hasMore = hasMore
        )
    }.flowOn(Dispatchers.IO)

    override fun loadTags(): Flow<Response<List<Tag>>> = repoTryCatchBlock {
        val tagsDto = tagsApi.getTags()
        tagsDto.map { tagDto ->
            Tag(name = tagDto.name)
        }
    }.flowOn(Dispatchers.IO)

    override fun loadCollectionRecipesId(id: String): Flow<Response<CollectionRecipes?>> = repoTryCatchBlock {
        val collectionDto = collectionsApi.getCollection(id)
        CollectionRecipes(recipeIds = collectionDto.recipeIds)
    }.flowOn(Dispatchers.IO)

    override fun loadCollectionRecipesCards(id: String): Flow<Response<List<Recipe>?>> = repoTryCatchBlock {
        val recipesDto = collectionsApi.getCollectionRecipes(id)
        recipesDto.map { dto -> convertDtoToRecipe(dto) }
    }.flowOn(Dispatchers.IO)

    override fun addRecipeToUserCollection(
        collectionId: String,
        recipeId: String,
        image: String?,
    ): Flow<Response<Unit>> = repoTryCatchBlock {
        collectionsApi.addRecipeToCollection(collectionId, recipeId)
    }.flowOn(Dispatchers.IO)

    override fun removeRecipeFromUserCollection(
        collectionId: String,
        recipeId: String,
    ): Flow<Response<Unit>> = repoTryCatchBlock {
        collectionsApi.removeRecipeFromCollection(collectionId, recipeId)
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
        try {
            recipesApi.addReview(id, ReviewCreateDto(mark = review.mark))
        } catch (e: HttpException) {
            if (e.code() == 409) {
                // Отзыв уже существует — обновляем через PUT
                recipesApi.updateMyReview(id, ReviewCreateDto(mark = review.mark))
            } else {
                throw e
            }
        }
        Unit
    }.flowOn(Dispatchers.IO)

    override fun updateReviewOnRecipe(
        id: String,
        oldReview: Review,
        newReview: Review,
    ): Flow<Response<Unit>> = repoTryCatchBlock {
        recipesApi.updateMyReview(id, ReviewCreateDto(mark = newReview.mark))
        Unit
    }.flowOn(Dispatchers.IO)

    override fun incrementCookedCounter(id: String): Flow<Response<Unit>> = repoTryCatchBlock {
        recipesApi.incrementCookedCounter(id)
    }.flowOn(Dispatchers.IO)
}
