package com.vk_edu.feed_and_eat.features.dishes.data

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
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

        val lastDocument = snapshot.documents[snapshot.size() - 1]
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
        sort: String,
        limit: Long,
        offset: Int,
        includedIngredients: List<String>,
        excludedIngredients: List<String>,
        tags: List<String>,
        caloriesMin: Double,
        caloriesMax: Double,
        sugarMin: Double,
        sugarMax: Double,
        proteinMin: Double,
        proteinMax: Double,
        fatMin: Double,
        fatMax: Double,
        carbohydratesMin: Double,
        carbohydratesMax: Double
    ): Flow<Response<List<Recipe>?>> = repoTryCatchBlock {
        var query = db.collection(RECIPES_COLLECTION)
//            .whereArrayContainsAny("tags", tags)
            .whereGreaterThanOrEqualTo("nutrients.Calories", caloriesMin)
            .whereLessThanOrEqualTo("nutrients.Calories", caloriesMax)
            .whereGreaterThanOrEqualTo("nutrients.Carbohydrates", carbohydratesMin)
            .whereLessThanOrEqualTo("nutrients.Carbohydrates", carbohydratesMax)
            .whereGreaterThanOrEqualTo("nutrients.Fat", fatMin)
            .whereLessThanOrEqualTo("nutrients.Fat", fatMax)
            .whereGreaterThanOrEqualTo("nutrients.Protein", proteinMin)
            .whereLessThanOrEqualTo("nutrients.Protein", proteinMax)
            .whereGreaterThanOrEqualTo("nutrients.Sugar", sugarMin)
            .whereLessThanOrEqualTo("nutrients.Sugar", sugarMax)
        if (sort == "newness") {
            query = query.orderBy("created", Query.Direction.DESCENDING)
        } else if (sort == "rating") {
            query = query.orderBy("rating", Query.Direction.DESCENDING)
        } else if (sort == "popularity") {
            query = query.orderBy("cooked", Query.Direction.DESCENDING)
        }
        val snapshot = query.limit(limit).get().await()

        val queryResult = mutableListOf<Recipe>()

        for (document in snapshot) {
            val recipe = document.toObject<Recipe>()
            queryResult.add(recipe)
        }
        return@repoTryCatchBlock queryResult
    }.flowOn(Dispatchers.IO)

    companion object {
        private const val RECIPES_COLLECTION = "recipes"
    }
}