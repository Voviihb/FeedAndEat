package com.vk_edu.feed_and_eat.features.login.data

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.login.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepoImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {
    private val ERROR_MESSAGE: String = "Error"
    override fun isUserAuthenticatedInFirebase() = auth.currentUser != null

    private suspend fun AuthRepoTryCatchBlock(func: suspend () -> Task<AuthResult>): Flow<Response<Boolean>> =
        flow {
            try {
                emit(Response.Loading)
                func().await()
                emit(Response.Success(true))
            } catch (e: Exception) {
                emit(Response.Failure(e))
            }
        }

    override suspend fun firebaseSignInAnonymously(): Flow<Response<Boolean>> =
        AuthRepoTryCatchBlock { auth.signInAnonymously() }.flowOn(Dispatchers.IO)

    override suspend fun firebaseSignUp(email: String, password: String): Flow<Response<Boolean>> =
        AuthRepoTryCatchBlock { auth.createUserWithEmailAndPassword(email, password) }
            .flowOn(Dispatchers.IO)

    override suspend fun firebaseSignIn(email: String, password: String): Flow<Response<Boolean>> =
        AuthRepoTryCatchBlock { auth.signInWithEmailAndPassword(email, password) }
            .flowOn(Dispatchers.IO)


    override suspend fun signOutAnonymous(): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            auth.currentUser?.delete()?.await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun signOut(): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            auth.signOut()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getFirebaseAuthState() = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }.flowOn(Dispatchers.IO)
}


