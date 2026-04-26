package com.vk_edu.feed_and_eat.server.mocks

import com.vk_edu.feed_and_eat.server.MockResponseBuilder
import com.vk_edu.feed_and_eat.server.MockServerMethod

/**
 * Объект-компаньон с билдерами для мок-ответов аутентификации.
 *
 * Использование:
 * ```kotlin
 * server.setMocks(
 *     AuthMockResponse.loginMock.success("login_test/auth_login_200.json"),
 * )
 * ```
 */
object AuthMockResponse {

    /** POST /auth/token — вход по логину/паролю */
    val loginMock = MockResponseBuilder(
        apiPath = "/auth/token",
        method = MockServerMethod.POST,
    )

    /** POST /auth/register — регистрация */
    val registerMock = MockResponseBuilder(
        apiPath = "/auth/register",
        method = MockServerMethod.POST,
    )

    /** POST /auth/refresh — обновление токена */
    val refreshMock = MockResponseBuilder(
        apiPath = "/auth/refresh",
        method = MockServerMethod.POST,
    )
}
