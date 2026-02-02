package com.vk_edu.feed_and_eat.features.dishes.data

import android.content.Context
import com.google.firebase.firestore.DocumentSnapshot
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
import com.vk_edu.feed_and_eat.network.api.RecipesApi
import com.vk_edu.feed_and_eat.features.network.api.CollectionsApi
import com.vk_edu.feed_and_eat.network.dto.IngredientDto
import com.vk_edu.feed_and_eat.network.dto.InstructionDto
import com.vk_edu.feed_and_eat.network.dto.NutrientsDto
import com.vk_edu.feed_and_eat.network.dto.RecipeDto
import com.vk_edu.feed_and_eat.network.dto.ServingsDto
import com.vk_edu.feed_and_eat.network.dto.TimerDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
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
    private val context: Context,
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
                            Timer(
                                type = timerDto.type,
                                lowerLimit = timerDto.lowerLimit,
                                upperLimit = timerDto.upperLimit,
                                number = timerDto.number,
                                id = timerDto.id ?: UUID.randomUUID().toString()
                            )
                        }
                    )
                },
                servings = dto.servings?.let { servingsDto ->
                    Servings(
                        amount = servingsDto.amount,
                        weight = servingsDto.weight
                    )
                },
                ingredients = dto.ingredients.map { ingredientDto ->
                    Ingredient(
                        name = ingredientDto.name,
                        amount = ingredientDto.amount,
                        unit = ingredientDto.unit
                    )
                },
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
                author = 0, // TODO: получать информацию об авторе
                user = dto.userId,
                rating = dto.rating,
                cooked = dto.cooked,
                reviews = null, // TODO: реализовать отзывы
                created = createdDate
            )
        } catch (e: Exception) {
            // Логируем ошибку конвертации
            android.util.Log.e("RecipesRepo", "Error converting DTO to Recipe: ${dto.name}", e)
            // Возвращаем базовый рецепт с минимальными данными
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
        documentSnapshot: DocumentSnapshot?,
    ): Flow<Response<PaginationResult>> = repoTryCatchBlock {
        // Для совместимости с Firebase - используем offset = 0
        val offset = 0

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
            sort = when (type?.name) {
                "RATING" -> "rating"
                "POPULARITY" -> "popularity"
                else -> "new"
            },
            limit = 20,
            offset = offset
        )

        PaginationResult(
            recipes = recipes.map { convertDtoToRecipe(it) },
            startDocument = null,
            endDocument = null,
            currentOffset = offset,
            hasMore = recipes.size >= 20 // Если получили полную страницу, возможно есть еще
        )
    }.flowOn(Dispatchers.IO)

    override fun loadSearchRecipes(
        filters: SearchFilters,
        type: Type?,
        offset: Int,
        limit: Int
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
            sort = when (type?.name) {
                "RATING" -> "rating"
                "POPULARITY" -> "popularity"
                else -> "new"
            },
            limit = limit,
            offset = offset
        )

        PaginationResult(
            recipes = recipes.map { convertDtoToRecipe(it) },
            currentOffset = offset,
            hasMore = recipes.size >= limit // Если получили полную страницу, возможно есть еще
        )
    }.flowOn(Dispatchers.IO)

    override fun loadTags(): Flow<Response<List<Tag>>> = repoTryCatchBlock {
        // TODO: реализовать получение тегов с backend
        emptyList<Tag>()
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
    ): Flow<Response<Void>> = repoTryCatchBlock {
        collectionsApi.addRecipeToCollection(collectionId, recipeId)
        Unit
    }.flowOn(Dispatchers.IO) as Flow<Response<Void>>

    override fun removeRecipeFromUserCollection(
        collectionId: String,
        recipeId: String,
    ): Flow<Response<Void>> = repoTryCatchBlock {
        collectionsApi.removeRecipeFromCollection(collectionId, recipeId)
        Unit
    }.flowOn(Dispatchers.IO) as Flow<Response<Void>>

    override fun createNewCollection(): Flow<Response<String>> = repoTryCatchBlock {
        // TODO: реализовать после миграции коллекций
        ""
    }.flowOn(Dispatchers.IO)

    override fun addNewReviewOnRecipe(id: String, review: Review): Flow<Response<Void>> = repoTryCatchBlock {
        // TODO: реализовать систему отзывов
        Unit
    }.flowOn(Dispatchers.IO) as Flow<Response<Void>>

    override fun updateReviewOnRecipe(
        id: String,
        oldReview: Review,
        newReview: Review,
    ): Flow<Response<Void>> = repoTryCatchBlock {
        // TODO: реализовать систему отзывов
        Unit
    }.flowOn(Dispatchers.IO) as Flow<Response<Void>>

    override fun incrementCookedCounter(id: String): Flow<Response<Void>> = repoTryCatchBlock {
        recipesApi.incrementCookedCounter(id)
        Unit
    }.flowOn(Dispatchers.IO) as Flow<Response<Void>>
}
