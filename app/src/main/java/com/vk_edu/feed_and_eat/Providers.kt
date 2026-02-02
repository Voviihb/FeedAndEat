package com.vk_edu.feed_and_eat

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoBackendImpl
import com.vk_edu.feed_and_eat.features.login.domain.repository.AuthRepository
import com.vk_edu.feed_and_eat.features.profile.data.UsersRepoBackendImpl
import com.vk_edu.feed_and_eat.features.profile.domain.repository.UsersRepository
import com.vk_edu.feed_and_eat.features.dishes.data.RecipesRepoBackendImpl
import com.vk_edu.feed_and_eat.features.dishes.domain.repository.RecipesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import com.vk_edu.feed_and_eat.network.api.UsersApi
import com.vk_edu.feed_and_eat.network.api.RecipesApi
import com.vk_edu.feed_and_eat.features.network.api.CollectionsApi
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
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

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
        @ApplicationContext context: Context
    ): RecipesRepository = RecipesRepoBackendImpl(recipesApi, collectionsApi, context)
}