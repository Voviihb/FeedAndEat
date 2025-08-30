package com.vk_edu.feed_and_eat

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vk_edu.feed_and_eat.features.login.data.AuthRepoBackendImpl
import com.vk_edu.feed_and_eat.features.login.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
}