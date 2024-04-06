package com.vk_edu.feed_and_eat.common.code

import com.vk_edu.feed_and_eat.features.login.domain.models.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun <T> repoTryCatchBlock(func: suspend () -> T): Flow<Response<T>> =
    flow {
        try {
            emit(Response.Loading)
            val res = func()
            emit(Response.Success(res))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }