package com.vk_edu.feed_and_eat.network

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.vk_edu.feed_and_eat.BuildConfig
import com.vk_edu.feed_and_eat.network.api.AuthApi
import com.vk_edu.feed_and_eat.network.api.UsersApi
import com.vk_edu.feed_and_eat.network.api.RecipesApi
import com.vk_edu.feed_and_eat.features.network.api.CollectionsApi
import com.vk_edu.feed_and_eat.features.network.api.TagsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.firstOrNull
import org.json.JSONObject

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideTokenStorage(@ApplicationContext context: Context): TokenStorage =
        TokenStorage(context)

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenStorage: TokenStorage): Interceptor = Interceptor { chain ->
        val token = runBlocking { tokenStorage.accessToken.firstOrNull() }
        val req = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token").build()
        } else chain.request()
        chain.proceed(req)
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(tokenStorage: TokenStorage): Authenticator =
        Authenticator { _, response ->
            // Защита от бесконечных попыток: если уже пытались обновить — останавливаемся
            if (response.request.header("X-Retry-After-Refresh") != null) {
                runBlocking { tokenStorage.clear() }
                return@Authenticator null
            }

            val refreshToken = runBlocking { tokenStorage.refreshToken.firstOrNull() }
            if (refreshToken.isNullOrBlank()) {
                runBlocking { tokenStorage.clear() }
                return@Authenticator null
            }

            // Делаем refresh-запрос через отдельный "чистый" клиент (без Authenticator)
            val refreshClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val jsonBody = JSONObject().put("refresh_token", refreshToken).toString()
            val refreshRequest = Request.Builder()
                .url("${BuildConfig.API_BASE_URL}auth/refresh")
                .post(jsonBody.toRequestBody("application/json".toMediaType()))
                .build()

            try {
                val refreshResponse = refreshClient.newCall(refreshRequest).execute()
                if (!refreshResponse.isSuccessful) {
                    runBlocking { tokenStorage.clear() }
                    return@Authenticator null
                }

                val body = refreshResponse.body?.string() ?: run {
                    runBlocking { tokenStorage.clear() }
                    return@Authenticator null
                }

                val json = JSONObject(body)
                val newAccessToken = json.optString("access_token", "")
                val newRefreshToken = json.optString("refresh_token", "")

                if (newAccessToken.isBlank()) {
                    runBlocking { tokenStorage.clear() }
                    return@Authenticator null
                }

                runBlocking { tokenStorage.saveTokens(newAccessToken, newRefreshToken) }

                // Повторяем исходный запрос с новым access token
                response.request.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .header("X-Retry-After-Refresh", "true")
                    .build()
            } catch (e: Exception) {
                runBlocking { tokenStorage.clear() }
                null
            }
        }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        tokenAuthenticator: Authenticator
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .authenticator(tokenAuthenticator)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideUsersApi(retrofit: Retrofit): UsersApi =
        retrofit.create(UsersApi::class.java)

    @Provides
    @Singleton
    fun provideRecipesApi(retrofit: Retrofit): RecipesApi =
        retrofit.create(RecipesApi::class.java)

    @Provides
    @Singleton
    fun provideCollectionsApi(retrofit: Retrofit): CollectionsApi =
        retrofit.create(CollectionsApi::class.java)

    @Provides
    @Singleton
    fun provideTagsApi(retrofit: Retrofit): TagsApi =
        retrofit.create(TagsApi::class.java)
}
