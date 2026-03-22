package com.vk_edu.feed_and_eat

import android.content.Context
import android.content.SharedPreferences
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoBackendImpl
import com.vk_edu.feed_and_eat.features.login.domain.repository.AuthRepository
import com.vk_edu.feed_and_eat.features.profile.data.UsersRepoBackendImpl
import com.vk_edu.feed_and_eat.features.profile.domain.repository.UsersRepository
import com.vk_edu.feed_and_eat.features.dishes.data.RecipesRepoBackendImpl
import com.vk_edu.feed_and_eat.features.dishes.domain.repository.RecipesRepository
import com.vk_edu.feed_and_eat.features.new_recipe.data.NewRecipeRepoBackendImpl
import com.vk_edu.feed_and_eat.features.new_recipe.repository.NewRecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import com.vk_edu.feed_and_eat.network.api.UsersApi
import com.vk_edu.feed_and_eat.network.api.RecipesApi
import com.vk_edu.feed_and_eat.features.network.api.CollectionsApi
import com.vk_edu.feed_and_eat.features.network.api.TagsApi
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppProviders {

    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext appContext: Context): SharedPreferences =
        appContext.getSharedPreferences(PreferencesManager.PROJECT_PREFS, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideAuthRepository(backendImpl: AuthRepoBackendImpl): AuthRepository = backendImpl


    @Provides
    @Singleton
    fun provideUsersRepository(
        usersApi: UsersApi,
        collectionsApi: CollectionsApi,
        @ApplicationContext context: Context
    ): UsersRepository = UsersRepoBackendImpl(usersApi, collectionsApi, context)

    @Provides
    @Singleton
    fun provideRecipesRepository(
        recipesApi: RecipesApi,
        collectionsApi: CollectionsApi,
        tagsApi: TagsApi,
    ): RecipesRepository = RecipesRepoBackendImpl(recipesApi, collectionsApi, tagsApi)
    
    @Provides
    @Singleton
    fun provideNewRecipeRepository(
        recipesApi: RecipesApi,
        @ApplicationContext context: Context
    ): NewRecipeRepository = NewRecipeRepoBackendImpl(recipesApi, context)
}