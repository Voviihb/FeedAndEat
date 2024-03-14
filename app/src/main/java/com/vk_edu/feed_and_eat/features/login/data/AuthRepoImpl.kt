package com.vk_edu.feed_and_eat.features.login.data

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
    override fun isUserAuthenticatedInFirebase() = auth.currentUser != null

    private fun <T> authRepoTryCatchBlock(func: suspend () -> T): Flow<Response<T>> =
        flow {
            try {
                emit(Response.Loading)
                val res = func()
                emit(Response.Success(res))
            } catch (e: Exception) {
                emit(Response.Failure(e))
            }
        }

    override fun firebaseSignInAnonymously(): Flow<Response<AuthResult>> =
        authRepoTryCatchBlock { auth.signInAnonymously().await() }.flowOn(Dispatchers.IO)

    override fun firebaseSignUp(email: String, password: String): Flow<Response<AuthResult>> =
        authRepoTryCatchBlock { auth.createUserWithEmailAndPassword(email, password).await() }
            .flowOn(Dispatchers.IO)

    override fun firebaseSignIn(email: String, password: String): Flow<Response<AuthResult>> =
        authRepoTryCatchBlock { auth.signInWithEmailAndPassword(email, password).await() }
            .flowOn(Dispatchers.IO)

    override fun signOutAnonymous(): Flow<Response<Void?>> =
        authRepoTryCatchBlock { auth.currentUser?.delete()?.await() }.flowOn(Dispatchers.IO)

    override fun signOut(): Flow<Response<Unit>> =
        authRepoTryCatchBlock { auth.signOut() }.flowOn(Dispatchers.IO)

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


