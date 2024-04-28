package com.vk_edu.feed_and_eat.features.dishes.domain.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.vk_edu.feed_and_eat.features.dishes.domain.models.SearchFilters
import com.vk_edu.feed_and_eat.features.dishes.domain.models.PaginationResult
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    fun loadRecipes(
        limit: Long,
        endOfPrevDocument: DocumentSnapshot?,
        startOfNextDocument: DocumentSnapshot?
    ): Flow<Response<PaginationResult>>

    fun loadRecipeById(id: String): Flow<Response<Recipe?>>

    fun loadSearchRecipes(
        filters: SearchFilters,
        endOfPrevDocument: DocumentSnapshot?,
        startOfNextDocument: DocumentSnapshot?
    ) : Flow<Response<PaginationResult>>

    fun loadTags(): Flow<Response<List<String>>>
}