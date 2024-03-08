package com.vk_edu.feed_and_eat.features.login.domain.repository

import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun isUserAuthenticatedInFirebase(): Boolean

    suspend fun firebaseSignInAnonymously(): Flow<Response<Boolean>>

    suspend fun firebaseSignUp(email: String, password: String): Flow<Response<Boolean>>

    suspend fun firebaseSignIn(email: String, password: String): Flow<Response<Boolean>>

    suspend fun signOutAnonymous(): Flow<Response<Boolean>>

    suspend fun signOut(): Flow<Response<Boolean>>

    fun getFirebaseAuthState(): Flow<Boolean>
}