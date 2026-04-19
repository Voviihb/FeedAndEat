package com.vk_edu.feed_and_eat.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.vk_edu.feed_and_eat.features.network.api.CollectionsApi
import com.vk_edu.feed_and_eat.features.network.api.TagsApi
import com.vk_edu.feed_and_eat.network.NetworkModule
import com.vk_edu.feed_and_eat.network.TokenStorage
import com.vk_edu.feed_and_eat.network.api.AuthApi
import com.vk_edu.feed_and_eat.network.api.RecipesApi
import com.vk_edu.feed_and_eat.network.api.UsersApi
import com.vk_edu.feed_and_eat.server.MockServerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Тестовый модуль Hilt, который заменяет [NetworkModule] в инструментальных тестах.
 *
 * Ключевое изменение: BASE_URL направляется на MockWebServer (localhost:8080),
 * а аутентификационные interceptor'ы и authenticator убираются — запросы идут
 * напрямую к мок-серверу без лишней логики.
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class],
)
object TestNetworkModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    /**
     * Предоставляем TokenStorage, т.к. [NetworkModule] его тоже предоставлял,
     * а мы полностью заменяем NetworkModule через @TestInstallIn.
     * В тестах хранилище токенов не используется (нет auth-interceptor'а).
     */
    @Provides
    @Singleton
    fun provideTokenStorage(@ApplicationContext context: Context): TokenStorage =
        TokenStorage(context)

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(MockServerManager.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideUsersApi(retrofit: Retrofit): UsersApi = retrofit.create(UsersApi::class.java)

    @Provides
    @Singleton
    fun provideRecipesApi(retrofit: Retrofit): RecipesApi = retrofit.create(RecipesApi::class.java)

    @Provides
    @Singleton
    fun provideCollectionsApi(retrofit: Retrofit): CollectionsApi =
        retrofit.create(CollectionsApi::class.java)

    @Provides
    @Singleton
    fun provideTagsApi(retrofit: Retrofit): TagsApi = retrofit.create(TagsApi::class.java)
}
