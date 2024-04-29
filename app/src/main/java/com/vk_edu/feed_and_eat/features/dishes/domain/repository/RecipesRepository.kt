package com.vk_edu.feed_and_eat.features.dishes.domain.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.vk_edu.feed_and_eat.features.dishes.domain.models.SearchFilters
import com.vk_edu.feed_and_eat.features.dishes.domain.models.PaginationResult
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Type
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    fun loadRecipeById(id: String): Flow<Response<Recipe?>>

    fun loadSearchRecipes(
        filters: SearchFilters,
        type: Type?,
        documentSnapshot: DocumentSnapshot?
    ) : Flow<Response<PaginationResult>>

    fun loadTags(): Flow<Response<List<String>>>
}