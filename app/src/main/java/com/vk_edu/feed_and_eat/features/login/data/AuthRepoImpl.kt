package com.vk_edu.feed_and_eat.features.login.data

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.login.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepoImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {
    override fun isUserAuthenticatedInFirebase() = auth.currentUser != null

    override fun getUserLogin(): String? = auth.currentUser?.displayName

    override fun getUserId(): String? = auth.currentUser?.uid

    override fun getUserEmail(): String? = auth.currentUser?.email

    override fun firebaseSignInAnonymously(): Flow<Response<AuthResult>> =
        repoTryCatchBlock { auth.signInAnonymously().await() }.flowOn(Dispatchers.IO)

    override fun firebaseSignUp(
        email: String,
        password: String,
        login: String
    ): Flow<Response<Void>> =
        repoTryCatchBlock {
            auth.createUserWithEmailAndPassword(email, password).await()
            val profileUpdates = userProfileChangeRequest {
                displayName = login
            }
            auth.currentUser!!.updateProfile(profileUpdates).await()
        }
            .flowOn(Dispatchers.IO)

    override fun firebaseSignIn(email: String, password: String): Flow<Response<AuthResult>> =
        repoTryCatchBlock { auth.signInWithEmailAndPassword(email, password).await() }
            .flowOn(Dispatchers.IO)

    override fun signOut(): Flow<Response<Unit>> =
        repoTryCatchBlock {
            val user = auth.currentUser
            if (user?.isAnonymous == true) {
                user.delete().await()
            }
            auth.signOut()
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