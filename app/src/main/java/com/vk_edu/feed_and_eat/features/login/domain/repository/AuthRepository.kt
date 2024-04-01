package com.vk_edu.feed_and_eat.features.login.domain.repository

import com.google.firebase.auth.AuthResult
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun isUserAuthenticatedInFirebase(): Boolean

    fun getCurrentUserId(): String?

    fun firebaseSignInAnonymously(): Flow<Response<AuthResult>>

    fun firebaseSignUp(email: String, password: String): Flow<Response<AuthResult>>

    fun firebaseSignIn(email: String, password: String): Flow<Response<AuthResult>>

    fun signOutAnonymous(): Flow<Response<Void?>>

    fun signOut(): Flow<Response<Unit>>

    fun getFirebaseAuthState(): Flow<Boolean>
}