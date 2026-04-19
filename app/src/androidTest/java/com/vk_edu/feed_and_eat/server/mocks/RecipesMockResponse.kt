package com.vk_edu.feed_and_eat.server.mocks

import com.vk_edu.feed_and_eat.server.MockResponseBuilder
import com.vk_edu.feed_and_eat.server.MockServerMethod

object RecipesMockResponse {

    /** GET /recipes/top — топ рецептов (используется на главном экране) */
    val topRecipesMock = MockResponseBuilder(
        apiPath = "/recipes/top",
        method = MockServerMethod.GET,
    )

    /** GET /recipes/latest — последние рецепты */
    val latestRecipesMock = MockResponseBuilder(
        apiPath = "/recipes/latest",
        method = MockServerMethod.GET,
    )

    /** GET /recipes/low_calorie — рецепты с низкой калорийностью */
    val lowCalorieRecipesMock = MockResponseBuilder(
        apiPath = "/recipes/low_calorie",
        method = MockServerMethod.GET,
    )

    /** GET /recipes/daily — рецепт дня */
    val dailyRecipeMock = MockResponseBuilder(
        apiPath = "/recipes/daily",
        method = MockServerMethod.GET,
    )

    /** GET /recipes/search — поиск рецептов */
    val searchRecipesMock = MockResponseBuilder(
        apiPath = "/recipes/search",
        method = MockServerMethod.GET,
    )
}
