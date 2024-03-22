package com.vk_edu.feed_and_eat

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn (SingletonComponent::class)
object AuthProvider {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }
}

@Module
@InstallIn (SingletonComponent::class)
object FireStoreProvider {
    @Provides
    @Singleton
    fun provideFireStore(): FirebaseFirestore {
        return Firebase.firestore
    }
}