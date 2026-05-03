package com.vk_edu.feed_and_eat.network.api

import com.vk_edu.feed_and_eat.features.network.dto.CreateRecipeDto
import com.vk_edu.feed_and_eat.network.dto.RecipeDto
import com.vk_edu.feed_and_eat.network.dto.ReviewCreateDto
import com.vk_edu.feed_and_eat.network.dto.ReviewDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipesApi {
    @POST("recipes/")
    suspend fun createRecipe(@Body recipe: CreateRecipeDto): RecipeDto

    @GET("recipes/{recipe_id}")
    suspend fun getRecipe(@Path("recipe_id") recipeId: String): RecipeDto

    @PUT("recipes/{recipe_id}")
    suspend fun updateRecipe(
        @Path("recipe_id") recipeId: String,
        @Body recipe: CreateRecipeDto
    ): RecipeDto

    @DELETE("recipes/{recipe_id}")
    suspend fun deleteRecipe(@Path("recipe_id") recipeId: String)

    @Multipart
    @POST("recipes/{recipe_id}/image")
    suspend fun uploadRecipeImage(
        @Path("recipe_id") recipeId: String,
        @Part image: MultipartBody.Part
    ): RecipeDto

    @GET("recipes/search")
    suspend fun searchRecipes(
        @Query("q") query: String? = null,
        @Query("calories_min") caloriesMin: Double? = null,
        @Query("calories_max") caloriesMax: Double? = null,
        @Query("protein_min") proteinMin: Double? = null,
        @Query("protein_max") proteinMax: Double? = null,
        @Query("fat_min") fatMin: Double? = null,
        @Query("fat_max") fatMax: Double? = null,
        @Query("carbs_min") carbsMin: Double? = null,
        @Query("carbs_max") carbsMax: Double? = null,
        @Query("sugar_min") sugarMin: Double? = null,
        @Query("sugar_max") sugarMax: Double? = null,
        @Query("tags") tags: List<String>? = null,
        @Query("sort") sort: String = "new",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): List<RecipeDto>

    @GET("recipes/top")
    suspend fun getTopRecipes(
        @Query("limit") limit: Int = 10
    ): List<RecipeDto>

    @GET("recipes/latest")
    suspend fun getLatestRecipes(
        @Query("limit") limit: Int = 10
    ): List<RecipeDto>

    @GET("recipes/low_calorie")
    suspend fun getLowCalorieRecipes(
        @Query("max_calories") maxCalories: Double = 300.0,
        @Query("limit") limit: Int = 10
    ): List<RecipeDto>

    @GET("recipes/daily")
    suspend fun getDailyRecipe(): RecipeDto

    @GET("users/me/recipes")
    suspend fun getMyRecipes(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): List<RecipeDto>

    @POST("recipes/{recipe_id}/cooked")
    suspend fun incrementCookedCounter(@Path("recipe_id") recipeId: String)

    @GET("recipes/{recipe_id}/reviews")
    suspend fun getRecipeReviews(@Path("recipe_id") recipeId: String): List<ReviewDto>

    @GET("recipes/{recipe_id}/reviews/my")
    suspend fun getMyReview(@Path("recipe_id") recipeId: String): ReviewDto?

    @POST("recipes/{recipe_id}/reviews")
    suspend fun addReview(
        @Path("recipe_id") recipeId: String,
        @Body review: ReviewCreateDto
    ): ReviewDto

    @PUT("recipes/{recipe_id}/reviews/my")
    suspend fun updateMyReview(
        @Path("recipe_id") recipeId: String,
        @Body review: ReviewCreateDto
    ): ReviewDto

    @DELETE("recipes/{recipe_id}/reviews/my")
    suspend fun deleteMyReview(@Path("recipe_id") recipeId: String)
}
