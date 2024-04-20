package com.vk_edu.feed_and_eat.features.dishes.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
import com.vk_edu.feed_and_eat.features.dishes.domain.models.FiltersDTO
import com.vk_edu.feed_and_eat.features.dishes.domain.models.PaginationResult
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.dishes.domain.repository.RecipesRepository
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesRepoImpl @Inject constructor(
    private val db: FirebaseFirestore
) : RecipesRepository {
    /**
     * Loads all recipes with pagination, order by DocID
     * @param count how many items to load
     * @param prevDocument is needed to calculate offset for pagination
     * */
    override fun loadRecipes(
        count: Long,
        prevDocument: DocumentSnapshot?
    ): Flow<Response<PaginationResult>> = repoTryCatchBlock {
        val snapshot = if (prevDocument != null) {
            db.collection(RECIPES_COLLECTION)
                .orderBy(FieldPath.documentId())
                .startAfter(prevDocument)
                .limit(count).get().await()
        } else {
            db.collection(RECIPES_COLLECTION)
                .orderBy(FieldPath.documentId())
                .limit(count).get().await()
        }

        val recipesList = mutableListOf<Recipe>()

        for (document in snapshot) {
            val dish = document.toObject<Recipe>()
            recipesList.add(dish)
        }

        val lastDocument =
            if (snapshot.size() > 0) snapshot.documents[snapshot.size() - 1] else prevDocument
        return@repoTryCatchBlock PaginationResult(
            recipesList.toList<Recipe>(),
            lastDocument
        )
    }.flowOn(Dispatchers.IO)


    override fun loadRecipeById(id: String): Flow<Response<Recipe?>> = repoTryCatchBlock {
        val document = db.collection(RECIPES_COLLECTION)
            .document(id).get().await()
        return@repoTryCatchBlock document.toObject<Recipe>()
    }.flowOn(Dispatchers.IO)

    override fun filterRecipes(
        filters: FiltersDTO,
        prevDocument: DocumentSnapshot?
    ): Flow<Response<PaginationResult>> = repoTryCatchBlock {
        var query = db.collection(RECIPES_COLLECTION)
            .whereGreaterThanOrEqualTo(NUTRIENTS_CALORIES_FIELD, filters.caloriesMin)
            .whereLessThanOrEqualTo(NUTRIENTS_CALORIES_FIELD, filters.caloriesMax)
            .whereGreaterThanOrEqualTo(NUTRIENTS_CARBOHYDRATES_FIELD, filters.carbohydratesMin)
            .whereLessThanOrEqualTo(NUTRIENTS_CARBOHYDRATES_FIELD, filters.carbohydratesMax)
            .whereGreaterThanOrEqualTo(NUTRIENTS_FAT_FIELD, filters.fatMin)
            .whereLessThanOrEqualTo(NUTRIENTS_FAT_FIELD, filters.fatMax)
            .whereGreaterThanOrEqualTo(NUTRIENTS_PROTEIN_FIELD, filters.proteinMin)
            .whereLessThanOrEqualTo(NUTRIENTS_PROTEIN_FIELD, filters.proteinMax)
            .whereGreaterThanOrEqualTo(NUTRIENTS_SUGAR_FIELD, filters.sugarMin)
            .whereLessThanOrEqualTo(NUTRIENTS_SUGAR_FIELD, filters.sugarMax)

        if (filters.tags != null) {
            query = query.whereArrayContainsAny(TAGS_FIELD, filters.tags)
        }

        when (filters.sort) {
            SORT_NEWNESS -> {
                query = query.orderBy(ORDER_BY_CREATED, Query.Direction.DESCENDING)
            }

            SORT_RATING -> {
                query = query.orderBy(ORDER_BY_RATING, Query.Direction.DESCENDING)
            }

            SORT_POPULARITY -> {
                query = query.orderBy(ORDER_BY_COOKED, Query.Direction.DESCENDING)
            }
        }

        val snapshot = if (prevDocument != null) {
            query.startAfter(prevDocument).limit(filters.limit).get().await()
        } else {
            query.limit(filters.limit).get().await()
        }

        val queryResult = mutableListOf<Recipe>()
        for (document in snapshot) {
            val recipe = document.toObject<Recipe>()
            queryResult.add(recipe)
        }
        val lastDocument =
            if (snapshot.size() > 0) snapshot.documents[snapshot.size() - 1] else prevDocument
        return@repoTryCatchBlock PaginationResult(
            queryResult,
            lastDocument
        )
    }.flowOn(Dispatchers.IO)

    companion object {
        private const val RECIPES_COLLECTION = "recipes"

        private const val SORT_NEWNESS = "newness"
        private const val SORT_RATING = "rating"
        private const val SORT_POPULARITY = "popularity"
        private const val ORDER_BY_CREATED = "created"
        private const val ORDER_BY_RATING = "rating"
        private const val ORDER_BY_COOKED = "cooked"

        private const val TAGS_FIELD = "tags"
        private const val INGREDIENTS_FIELD = "ingredients"
        private const val NUTRIENTS_CALORIES_FIELD = "nutrients.Calories"
        private const val NUTRIENTS_CARBOHYDRATES_FIELD = "nutrients.Carbohydrates"
        private const val NUTRIENTS_FAT_FIELD = "nutrients.Fat"
        private const val NUTRIENTS_PROTEIN_FIELD = "nutrients.Protein"
        private const val NUTRIENTS_SUGAR_FIELD = "nutrients.Sugar"
    }
}