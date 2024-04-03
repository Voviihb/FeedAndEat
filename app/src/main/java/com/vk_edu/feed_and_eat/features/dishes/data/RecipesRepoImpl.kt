package com.vk_edu.feed_and_eat.features.dishes.data

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
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
    ): Flow<Response<Pair<List<Recipe>, DocumentSnapshot?>>> = repoTryCatchBlock {
        val snapshot = if (prevDocument != null) {
            db.collection("recipes")
                .orderBy(FieldPath.documentId())
                .startAfter(prevDocument)
                .limit(count).get().await()
        } else {
            db.collection("recipes")
                .orderBy(FieldPath.documentId())
                .limit(count).get().await()
        }

        val recipesList = mutableListOf<Recipe>()

        for (document in snapshot) {
            val dish = document.toObject<Recipe>()
            recipesList.add(dish)
        }

        val lastDocument = snapshot.documents[snapshot.size() - 1]
        return@repoTryCatchBlock Pair<List<Recipe>, DocumentSnapshot?>(
            recipesList.toList<Recipe>(),
            lastDocument
        )
    }.flowOn(Dispatchers.IO)

    /**
     * Loads one recipe with doc Id provided, if document exists
     * @param id document ID
     * */
    override fun loadRecipeById(id: String): Flow<Response<Recipe?>> = repoTryCatchBlock {
        val document = db.collection("recipes")
            .document(id).get().await()
        return@repoTryCatchBlock document.toObject<Recipe>()
    }.flowOn(Dispatchers.IO)
}