package com.vk_edu.feed_and_eat.features.login.data


import com.vk_edu.feed_and_eat.common.code.repoTryCatchBlock
import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import com.vk_edu.feed_and_eat.features.login.domain.repository.AuthRepository
import com.vk_edu.feed_and_eat.network.TokenStorage
import com.vk_edu.feed_and_eat.network.api.AuthApi
import com.vk_edu.feed_and_eat.network.api.RegisterBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepoBackendImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenStorage: TokenStorage
) : AuthRepository {

    override fun isAuthorized(): Flow<Boolean> = tokenStorage.accessToken.map { it != null }

    override fun getCurrentUsername(): String? = null
    override fun getCurrentUserId(): String? = null
    override fun getCurrentEmail(): String? = null

    override fun signUp(email: String, password: String, username: String): Flow<Response<Unit>> =
        repoTryCatchBlock {
            val res = api.register(RegisterBody(email, password, username))
            tokenStorage.saveTokens(res.access, res.refresh ?: "")
        }.flowOn(Dispatchers.IO)

    override fun signIn(email: String, password: String): Flow<Response<Unit>> =
        repoTryCatchBlock {
            val token = api.token(email, password)
            tokenStorage.saveTokens(token.access, token.refresh ?: "")
        }.flowOn(Dispatchers.IO)

    override fun signOut(): Flow<Response<Unit>> =
        repoTryCatchBlock { tokenStorage.clear() }.flowOn(Dispatchers.IO)
}
