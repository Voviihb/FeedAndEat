package com.vk_edu.feed_and_eat.features.dishes.domain.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.vk_edu.feed_and_eat.features.dishes.domain.models.PaginationResult
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    fun loadRecipes(
        count: Long = 10,
        prevDocument: DocumentSnapshot?
    ): Flow<Response<PaginationResult>>

    fun loadRecipeById(id: String): Flow<Response<Recipe?>>

    fun filterRecipes(
        sort: String,
        limit: Long,
        prevDocument: DocumentSnapshot?,
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
        carbohydratesMax: Double,
    ) : Flow<Response<PaginationResult>>
}