package com.vk_edu.feed_and_eat.network.api

import com.vk_edu.feed_and_eat.network.dto.TokenDto
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Field
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body body: RegisterBody): TokenDto

    @FormUrlEncoded
    @POST("auth/token")
    suspend fun token(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = "password"
    ): TokenDto

    @FormUrlEncoded
    @POST("auth/refresh")
    suspend fun refresh(
        @Field("refresh_token") refresh: String,
        @Field("grant_type") grantType: String = "refresh_token"
    ): TokenDto
}

@Serializable
data class RegisterBody(val email: String, val password: String, val username: String)
