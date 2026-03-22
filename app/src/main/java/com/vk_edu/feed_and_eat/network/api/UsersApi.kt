package com.vk_edu.feed_and_eat.network.api

import com.vk_edu.feed_and_eat.network.dto.ProfileUpdateDto
import com.vk_edu.feed_and_eat.network.dto.UserDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UsersApi {
    @GET("users/me")
    suspend fun getMyProfile(): UserDto

    @GET("users/{user_id}")
    suspend fun getUserProfile(@Path("user_id") userId: String): UserDto

    @PUT("users/me")
    suspend fun updateMyProfile(@Body profileUpdate: ProfileUpdateDto): UserDto

    @Multipart
    @POST("users/me/avatar")
    suspend fun uploadAvatar(@Part avatar: MultipartBody.Part): UserDto
}
