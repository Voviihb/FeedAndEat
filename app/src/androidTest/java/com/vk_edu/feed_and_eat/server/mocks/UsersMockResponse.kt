package com.vk_edu.feed_and_eat.server.mocks

import com.vk_edu.feed_and_eat.server.MockResponseBuilder
import com.vk_edu.feed_and_eat.server.MockServerMethod

/**
 * Объект-компаньон с билдерами для мок-ответов пользовательских данных.
 */
object UsersMockResponse {

    /** GET /collections/my — коллекции текущего пользователя (избранное и пр.) */
    val myCollectionsMock = MockResponseBuilder(
        apiPath = "/collections/my",
        method = MockServerMethod.GET,
    )

    /** GET /users/me — профиль текущего пользователя */
    val myProfileMock = MockResponseBuilder(
        apiPath = "/users/me",
        method = MockServerMethod.GET,
    )

    /** GET /users/me/recipes — рецепты текущего пользователя */
    val myRecipesMock = MockResponseBuilder(
        apiPath = "/users/me/recipes",
        method = MockServerMethod.GET,
    )
}
