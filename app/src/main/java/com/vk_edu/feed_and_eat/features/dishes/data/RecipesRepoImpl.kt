package com.vk_edu.feed_and_eat.features.dishes.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
import com.vk_edu.feed_and_eat.features.dishes.domain.models.CollectionRecipes
import com.vk_edu.feed_and_eat.features.dishes.domain.models.DailyRecipes
import com.vk_edu.feed_and_eat.features.dishes.domain.models.PaginationResult
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Review
import com.vk_edu.feed_and_eat.features.dishes.domain.models.SearchFilters
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Tag
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Type
import com.vk_edu.feed_and_eat.features.dishes.domain.repository.RecipesRepository
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.profile.domain.models.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesRepoImpl @Inject constructor(
    private val db: FirebaseFirestore
) : RecipesRepository {
    override fun loadRecipeById(id: String): Flow<Response<Recipe?>> = repoTryCatchBlock {
        val query = db.collection(RECIPES_COLLECTION).document(id)
        val document = query.get().await()
        return@repoTryCatchBlock document.toObject<Recipe>()?.copy(id = id)
    }.flowOn(Dispatchers.IO)

    override fun loadDailyRecipe(): Flow<Response<Recipe?>> = repoTryCatchBlock {
        val dayOfYear = LocalDate.now().dayOfYear
        val query = db.collection(DAILY_RECIPE_COLLECTION).whereEqualTo(DAY_OF_YEAR, dayOfYear)
        val dailyRecipes = query.get().await()
        val recipesOfDay = dailyRecipes.map { it.toObject<DailyRecipes>() }[0].recipesOfDay

        val currentYear = LocalDate.now().year
        val recipeId = recipesOfDay[currentYear % recipesOfDay.size]
        val document = db.collection(RECIPES_COLLECTION).document(recipeId).get().await()
        return@repoTryCatchBlock document.toObject<Recipe>()?.copy(id = recipeId)
    }.flowOn(Dispatchers.IO)

    override fun loadTopRatingRecipes(): Flow<Response<List<Recipe>>> = repoTryCatchBlock {
        val query = db.collection(RECIPES_COLLECTION).orderBy(RATING_FIELD, Query.Direction.DESCENDING)
        val recipes = query.limit(LIMIT_OF_ROW).get().await()
        return@repoTryCatchBlock recipes.map { it.toObject<Recipe>().copy(id = it.id) }
    }.flowOn(Dispatchers.IO)

    override fun loadLowCalorieRecipes(): Flow<Response<List<Recipe>>> = repoTryCatchBlock {
        val query = db.collection(RECIPES_COLLECTION)
            .whereLessThanOrEqualTo(NUTRIENTS_CALORIES_FIELD, MAX_CALORIES)
            .orderBy(COOKED_FIELD, Query.Direction.DESCENDING)
        val recipes = query.limit(LIMIT_OF_ROW).get().await()
        return@repoTryCatchBlock recipes.map { it.toObject<Recipe>().copy(id = it.id) }
    }.flowOn(Dispatchers.IO)

    override fun loadLastAddedRecipes(): Flow<Response<List<Recipe>>> = repoTryCatchBlock {
        val query = db.collection(RECIPES_COLLECTION).orderBy(CREATED_FIELD, Query.Direction.DESCENDING)
        val recipes = query.limit(LIMIT_OF_ROW).get().await()
        return@repoTryCatchBlock recipes.map { it.toObject<Recipe>().copy(id = it.id) }
    }.flowOn(Dispatchers.IO)

    override fun loadBreakfastRecipes(): Flow<Response<List<Recipe>>> = repoTryCatchBlock {
        val query = db.collection(RECIPES_COLLECTION)
            .whereArrayContains(TAGS_FIELD, BREAKFAST_TAG)
            .orderBy(COOKED_FIELD, Query.Direction.DESCENDING)
        val recipes = query.limit(LIMIT_OF_ROW).get().await()
        return@repoTryCatchBlock recipes.map { it.toObject<Recipe>().copy(id = it.id) }
    }.flowOn(Dispatchers.IO)

    /**
     * Filters recipes with pagination
     * @param filters pass all filters in DTO here
     * */
    override fun loadSearchRecipes(
        filters: SearchFilters,
        type: Type?,
        documentSnapshot: DocumentSnapshot?
    ): Flow<Response<PaginationResult>> = repoTryCatchBlock {
        var query = db.collection(RECIPES_COLLECTION)
            .where(
                Filter.and(
                    Filter.greaterThanOrEqualTo(NUTRIENTS_CALORIES_FIELD, filters.caloriesMin),
                    Filter.lessThanOrEqualTo(NUTRIENTS_CALORIES_FIELD, filters.caloriesMax),
                    Filter.greaterThanOrEqualTo(
                        NUTRIENTS_CARBOHYDRATES_FIELD,
                        filters.carbohydratesMin
                    ),
                    Filter.lessThanOrEqualTo(
                        NUTRIENTS_CARBOHYDRATES_FIELD,
                        filters.carbohydratesMax
                    ),
                    Filter.greaterThanOrEqualTo(NUTRIENTS_FAT_FIELD, filters.fatMin),
                    Filter.lessThanOrEqualTo(NUTRIENTS_FAT_FIELD, filters.fatMax),
                    Filter.greaterThanOrEqualTo(NUTRIENTS_PROTEIN_FIELD, filters.proteinMin),
                    Filter.lessThanOrEqualTo(NUTRIENTS_PROTEIN_FIELD, filters.proteinMax),
                    Filter.greaterThanOrEqualTo(NUTRIENTS_SUGAR_FIELD, filters.sugarMin),
                    Filter.lessThanOrEqualTo(NUTRIENTS_SUGAR_FIELD, filters.sugarMax),
                )
            )

        if (filters.startsWith != "")
            query = query.whereGreaterThanOrEqualTo(NAME_FIELD, filters.startsWith)
                .whereLessThanOrEqualTo(NAME_FIELD, "${filters.startsWith}\uf8ff")
        if (filters.tags.isNotEmpty())
            query = query.whereArrayContainsAny(TAGS_FIELD, filters.tags)

        query = when (filters.sort) {
            SORT_NEWNESS -> {
                query.orderBy(CREATED_FIELD, Query.Direction.DESCENDING)
            }

            SORT_RATING -> {
                query.orderBy(RATING_FIELD, Query.Direction.DESCENDING)
            }

            SORT_POPULARITY -> {
                query.orderBy(COOKED_FIELD, Query.Direction.DESCENDING)
            }

            else -> {
                query.orderBy(CREATED_FIELD, Query.Direction.DESCENDING)
            }
        }

        val snapshot = when (type) {
            Type.EXCLUDED_FIRST -> {
                query.startAfter(documentSnapshot!!).limit(filters.limit.toLong()).get().await()
            }

            Type.INCLUDED_FIRST -> {
                query.startAt(documentSnapshot!!).limit(filters.limit.toLong()).get().await()
            }

            Type.INCLUDED_LAST -> {
                query.endAt(documentSnapshot!!).limitToLast(filters.limit.toLong()).get().await()
            }

            Type.EXCLUDED_LAST -> {
                query.endBefore(documentSnapshot!!).limitToLast(filters.limit.toLong()).get()
                    .await()
            }

            else -> {
                query.limit(filters.limit.toLong()).get().await()
            }
        }

        return@repoTryCatchBlock PaginationResult(
            recipes = snapshot.map { it.toObject<Recipe>().copy(id = it.id) },
            startDocument = snapshot.documents.ifEmpty { null }?.get(0),
            endDocument = snapshot.documents.ifEmpty { null }?.get(snapshot.size() - 1)
        )
    }.flowOn(Dispatchers.IO)

    override fun loadTags(): Flow<Response<List<Tag>>> = repoTryCatchBlock {
        val query = db.collection(TAGS_COLLECTION)
        val tags = query.get().await()
        return@repoTryCatchBlock tags.map { it.toObject<Tag>() }
    }.flowOn(Dispatchers.IO)

    override fun loadCollectionRecipes(id: String): Flow<Response<CollectionRecipes?>> =
        repoTryCatchBlock {
            val query = db.collection(COLLECTIONS_COLLECTION).document(id)
            val collections = query.get().await()
            return@repoTryCatchBlock collections.toObject<CollectionRecipes>()
        }.flowOn(Dispatchers.IO)

    /**
     * Adds new recipes to already existing collection
     * @param collectionId pass here id of collection
     * @param recipeId pass here new recipe id
     * */
    override fun addRecipeToUserCollection(
        userId: String,
        collectionId: String,
        recipeId: String,
        image: String?
    ): Flow<Response<Void>> = repoTryCatchBlock {
        val docRef = db.collection(COLLECTIONS_COLLECTION).document(collectionId)
        docRef.update(RECIPE_IDS_FIELD, FieldValue.arrayUnion(recipeId)).await()
        val userDoc = db.collection(USERS_COLLECTION).document(userId)
        val user = userDoc.get().await().toObject<UserModel>()
        user?.collectionsIdList?.filter { it.id == collectionId }?.map { it.picture = image }
        userDoc.update(COLLECTIONS_ID_LIST_FIELD, user?.collectionsIdList).await()
    }.flowOn(Dispatchers.IO)

    override fun removeRecipeFromUserCollection(
        collectionId: String,
        recipeId: String,
    ): Flow<Response<Void>> = repoTryCatchBlock {
        val docRef = db.collection(COLLECTIONS_COLLECTION).document(collectionId)
        docRef.update(RECIPE_IDS_FIELD, FieldValue.arrayRemove(recipeId)).await()
    }.flowOn(Dispatchers.IO)

    override fun createNewCollection(): Flow<Response<String>> = repoTryCatchBlock {
        val result = db.collection(COLLECTIONS_COLLECTION).document()
        result.set(
            hashMapOf<String, List<String>>(RECIPE_IDS_FIELD to listOf())
        ).await()
        return@repoTryCatchBlock result.id
    }.flowOn(Dispatchers.IO)

    override fun addNewReviewOnRecipe(id: String, review: Review): Flow<Response<Void>> =
        repoTryCatchBlock {
            val recipeDoc = db.collection(RECIPES_COLLECTION).document(id)
            val recipe = recipeDoc.get().await().toObject<Recipe>()
            var newRating = 0.0
            if (recipe != null) {
                newRating = (recipe.rating * (recipe.reviews?.size ?: 0)
                        + review.mark) / ((recipe.reviews?.size ?: 0) + 1)
            }
            recipeDoc.update(
                REVIEWS_LIST_FIELD, FieldValue.arrayUnion(review),
                RATING_FIELD, newRating
            ).await()
        }.flowOn(Dispatchers.IO)

    override fun updateReviewOnRecipe(
        id: String,
        oldReview: Review,
        newReview: Review
    ): Flow<Response<Void>> =
        repoTryCatchBlock {
            val recipeDoc = db.collection(RECIPES_COLLECTION).document(id)
            val recipe = recipeDoc.get().await().toObject<Recipe>()
            var newRating = 0.0
            if (recipe != null) {
                newRating = (recipe.rating * (recipe.reviews?.size ?: 0)
                        - oldReview.mark + newReview.mark) / ((recipe.reviews?.size ?: 0))
            }
            recipeDoc.update(
                REVIEWS_LIST_FIELD, FieldValue.arrayRemove(oldReview),
                REVIEWS_LIST_FIELD, FieldValue.arrayUnion(newReview),
                RATING_FIELD, newRating
            ).await()
        }.flowOn(Dispatchers.IO)

    override fun incrementCookedCounter(id: String): Flow<Response<Void>> = repoTryCatchBlock {
        val recipeDoc = db.collection(RECIPES_COLLECTION).document(id)
        recipeDoc.update(COOKED_FIELD, FieldValue.increment(1)).await()
    }

    companion object {
        private const val TAGS_COLLECTION = "tags"
        private const val RECIPES_COLLECTION = "recipes"
        private const val COLLECTIONS_COLLECTION = "collections"
        private const val USERS_COLLECTION = "users"
        private const val DAILY_RECIPE_COLLECTION = "daily_recipe"

        private const val DAY_OF_YEAR = "dayOfYear"
        private const val BREAKFAST_TAG = "breakfast"
        private const val MAX_CALORIES = 100.0
        private const val LIMIT_OF_ROW = 10L

        private const val REVIEWS_LIST_FIELD = "reviews"

        private const val NAME_FIELD = "name"
        private const val TAGS_FIELD = "tags"
        private const val NUTRIENTS_CALORIES_FIELD = "nutrients.Calories"
        private const val NUTRIENTS_CARBOHYDRATES_FIELD = "nutrients.Carbohydrates"
        private const val NUTRIENTS_FAT_FIELD = "nutrients.Fat"
        private const val NUTRIENTS_PROTEIN_FIELD = "nutrients.Protein"
        private const val NUTRIENTS_SUGAR_FIELD = "nutrients.Sugar"
        private const val CREATED_FIELD = "created"
        private const val RATING_FIELD = "rating"
        private const val COOKED_FIELD = "cooked"
        private const val SORT_NEWNESS = 0
        private const val SORT_RATING = 1
        private const val SORT_POPULARITY = 2

        private const val RECIPE_IDS_FIELD = "recipeIds"
        private const val COLLECTIONS_ID_LIST_FIELD = "collectionsIdList"
    }
}