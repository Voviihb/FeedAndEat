package com.vk_edu.feed_and_eat.features.dishes.domain.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.vk_edu.feed_and_eat.features.dishes.domain.models.CollectionRecipes
import com.vk_edu.feed_and_eat.features.dishes.domain.models.PaginationResult
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Recipe
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Review
import com.vk_edu.feed_and_eat.features.dishes.domain.models.SearchFilters
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Tag
import com.vk_edu.feed_and_eat.features.dishes.domain.models.Type
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    fun loadRecipeById(id: String): Flow<Response<Recipe?>>

    fun loadDailyRecipe(): Flow<Response<Recipe?>>

    fun loadTopRatingRecipes(): Flow<Response<List<Recipe>>>

    fun loadLowCalorieRecipes(): Flow<Response<List<Recipe>>>

    fun loadLastAddedRecipes(): Flow<Response<List<Recipe>>>

    fun loadBreakfastRecipes(): Flow<Response<List<Recipe>>>

    fun loadSearchRecipes(
        filters: SearchFilters,
        type: Type?,
        documentSnapshot: DocumentSnapshot?
    ): Flow<Response<PaginationResult>>

    fun loadTags(): Flow<Response<List<Tag>>>

    fun loadCollectionRecipesId(id: String): Flow<Response<CollectionRecipes?>>

    fun loadCollectionRecipesCards(id: String): Flow<Response<List<Recipe>?>>

    fun addRecipeToUserCollection(
        userId: String,
        collectionId: String,
        recipeId: String,
        image: String? = null
    ): Flow<Response<Void>>

    fun removeRecipeFromUserCollection(
        collectionId: String,
        recipeId: String,
    ): Flow<Response<Void>>

    fun createNewCollection(): Flow<Response<String>>

    fun addNewReviewOnRecipe(id: String, review: Review): Flow<Response<Void>>

    fun updateReviewOnRecipe(id: String, oldReview: Review, newReview: Review): Flow<Response<Void>>

    fun incrementCookedCounter(id: String): Flow<Response<Void>>
}