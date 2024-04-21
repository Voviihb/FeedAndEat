package com.vk_edu.feed_and_eat.features.dishes.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
import com.vk_edu.feed_and_eat.features.dishes.domain.models.FiltersDTO
import com.vk_edu.feed_and_eat.features.dishes.domain.models.PaginationResult
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.dishes.domain.models.SortFilter
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
     * @param limit how many items to load
     * @param startOfNextDocument is needed to calculate start point for FORWARD pagination
     * @param endOfPrevDocument is needed to calculate start point for BACKWARD pagination
     * */
    override fun loadRecipes(
        limit: Long,
        endOfPrevDocument: DocumentSnapshot?,
        startOfNextDocument: DocumentSnapshot?
    ): Flow<Response<PaginationResult>> = repoTryCatchBlock {
        val query = db.collection(RECIPES_COLLECTION).orderBy(FieldPath.documentId())
        val snapshot = if (startOfNextDocument == null && endOfPrevDocument == null) {
            query.limit(limit).get().await()
        } else if (startOfNextDocument != null) {
            query.startAfter(startOfNextDocument).limit(limit).get().await()
        } else {
            query.endBefore(endOfPrevDocument).limitToLast(limit).get().await()
        }

        val queryResult = mutableListOf<Recipe>()
        for (document in snapshot) {
            val recipe = document.toObject<Recipe>()
            queryResult.add(recipe)
        }
        val startDocument = if (snapshot.size() > 0) snapshot.documents[0] else null
        val endDocument = if (snapshot.size().toLong() == limit) snapshot.documents[snapshot.size() - 1] else null
        return@repoTryCatchBlock PaginationResult(
            recipes = queryResult,
            endOfPrevDocument = startDocument,
            startOfNextDocument = endDocument
        )
    }.flowOn(Dispatchers.IO)


    override fun loadRecipeById(id: String): Flow<Response<Recipe?>> = repoTryCatchBlock {
        val document = db.collection(RECIPES_COLLECTION)
            .document(id).get().await()
        return@repoTryCatchBlock document.toObject<Recipe>()
    }.flowOn(Dispatchers.IO)

    /**
     * Filters recipes with pagination
     * @param filters pass all filters in DTO here
     * @param startOfNextDocument is needed to calculate start point for FORWARD pagination
     * @param endOfPrevDocument is needed to calculate start point for BACKWARD pagination
     * */
    override fun filterRecipes(
        filters: FiltersDTO,
        endOfPrevDocument: DocumentSnapshot?,
        startOfNextDocument: DocumentSnapshot?
    ): Flow<Response<PaginationResult>> = repoTryCatchBlock {
        var query = db.collection(RECIPES_COLLECTION)
            .where(Filter.and(
                Filter.greaterThanOrEqualTo(NUTRIENTS_CALORIES_FIELD, filters.caloriesMin),
                Filter.lessThanOrEqualTo(NUTRIENTS_CALORIES_FIELD, filters.caloriesMax),
                Filter.greaterThanOrEqualTo(NUTRIENTS_CARBOHYDRATES_FIELD, filters.carbohydratesMin),
                Filter.lessThanOrEqualTo(NUTRIENTS_CARBOHYDRATES_FIELD, filters.carbohydratesMax),
                Filter.greaterThanOrEqualTo(NUTRIENTS_FAT_FIELD, filters.fatMin),
                Filter.lessThanOrEqualTo(NUTRIENTS_FAT_FIELD, filters.fatMax),
                Filter.greaterThanOrEqualTo(NUTRIENTS_PROTEIN_FIELD, filters.proteinMin),
                Filter.lessThanOrEqualTo(NUTRIENTS_PROTEIN_FIELD, filters.proteinMax),
                Filter.greaterThanOrEqualTo(NUTRIENTS_SUGAR_FIELD, filters.sugarMin),
                Filter.lessThanOrEqualTo(NUTRIENTS_SUGAR_FIELD, filters.sugarMax),
            ))

        if (filters.startsWith != null) {
            query = query.whereGreaterThanOrEqualTo(NAME_FIELD, filters.startsWith)
                .whereLessThanOrEqualTo(NAME_FIELD, "${filters.startsWith}\\uf8ff")
        }
        if (filters.tags != null)
            query = query.whereArrayContainsAny(TAGS_FIELD, filters.tags)

        query = when (filters.sort) {
            SortFilter.SORT_NEWNESS -> {
                query.orderBy(ORDER_BY_CREATED, Query.Direction.DESCENDING)
            }
            SortFilter.SORT_RATING -> {
                query.orderBy(ORDER_BY_RATING, Query.Direction.DESCENDING)
            }

            SortFilter.SORT_POPULARITY -> {
                query.orderBy(ORDER_BY_COOKED, Query.Direction.DESCENDING)
            }
        }

        val snapshot = if (startOfNextDocument == null && endOfPrevDocument == null) {
            query.limit(filters.limit).get().await()
        } else if (startOfNextDocument != null) {
            query.startAfter(startOfNextDocument).limit(filters.limit).get().await()
        } else {
            query.endBefore(endOfPrevDocument).limitToLast(filters.limit).get().await()
        }
        
        val queryResult = mutableListOf<Recipe>()
        for (document in snapshot) {
            val recipe = document.toObject<Recipe>()
            queryResult.add(recipe)
        }
        val startDocument = if (snapshot.size() > 0) snapshot.documents[0] else null
        val endDocument = if (snapshot.size().toLong() == filters.limit) snapshot.documents[snapshot.size() - 1] else null
        return@repoTryCatchBlock PaginationResult(
            recipes = queryResult,
            endOfPrevDocument = startDocument,
            startOfNextDocument = endDocument
        )
    }.flowOn(Dispatchers.IO)

    companion object {
        private const val RECIPES_COLLECTION = "recipes"

        private const val ORDER_BY_CREATED = "created"
        private const val ORDER_BY_RATING = "rating"
        private const val ORDER_BY_COOKED = "cooked"

        private const val NAME_FIELD = "name"
        private const val TAGS_FIELD = "tags"
        private const val NUTRIENTS_CALORIES_FIELD = "nutrients.Calories"
        private const val NUTRIENTS_CARBOHYDRATES_FIELD = "nutrients.Carbohydrates"
        private const val NUTRIENTS_FAT_FIELD = "nutrients.Fat"
        private const val NUTRIENTS_PROTEIN_FIELD = "nutrients.Protein"
        private const val NUTRIENTS_SUGAR_FIELD = "nutrients.Sugar"
    }
}