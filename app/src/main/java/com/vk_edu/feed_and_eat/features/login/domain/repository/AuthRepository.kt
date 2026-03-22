package com.vk_edu.feed_and_eat.features.login.domain.repository

import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    /** Проверка: есть ли сейчас access-токен */
    fun isAuthorized(): Flow<Boolean>

    fun getCurrentUsername(): String?
    fun getCurrentUserId(): String?
    fun getCurrentEmail(): String?

    fun signUp(email: String, password: String, username: String): Flow<Response<Unit>>
    fun signIn(email: String, password: String): Flow<Response<Unit>>
    fun signOut(): Flow<Response<Unit>>
}