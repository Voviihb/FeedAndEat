package com.vk_edu.feed_and_eat.features.network.api

import com.vk_edu.feed_and_eat.features.network.dto.CollectionBriefDto
import com.vk_edu.feed_and_eat.features.network.dto.CollectionDto
import com.vk_edu.feed_and_eat.features.network.dto.CreateCollectionBody
import com.vk_edu.feed_and_eat.network.dto.RecipeDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CollectionsApi {
    
    @POST("collections/")
    suspend fun createCollection(@Body body: CreateCollectionBody): CollectionDto
    
    @GET("collections/my")
    suspend fun getMyCollections(): List<CollectionBriefDto>
    
    @GET("collections/{collection_id}")
    suspend fun getCollection(@Path("collection_id") collectionId: String): CollectionDto
    
    @GET("collections/{collection_id}/recipes")
    suspend fun getCollectionRecipes(@Path("collection_id") collectionId: String): List<RecipeDto>
    
    @POST("collections/{collection_id}/recipes/{recipe_id}")
    suspend fun addRecipeToCollection(
        @Path("collection_id") collectionId: String,
        @Path("recipe_id") recipeId: String
    )
    
    @DELETE("collections/{collection_id}/recipes/{recipe_id}")
    suspend fun removeRecipeFromCollection(
        @Path("collection_id") collectionId: String,
        @Path("recipe_id") recipeId: String
    )
}