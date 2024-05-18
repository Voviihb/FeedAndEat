package com.vk_edu.feed_and_eat

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthProvider {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }
}

@Module
@InstallIn(SingletonComponent::class)
object FireStoreProvider {
    @Provides
    @Singleton
    fun provideFireStore(): FirebaseFirestore {
        return Firebase.firestore
    }
}

@Module
@InstallIn(SingletonComponent::class)
object CloudStorageProvider {
    @Provides
    @Singleton
    fun provideCloudStorage(): FirebaseStorage {
        return Firebase.storage
    }
}


@Module
@InstallIn(SingletonComponent::class)
object SharedPrefsProvider {
    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(
            PreferencesManager.PROJECT_PREFS,
            Context.MODE_PRIVATE
        )
    }
}