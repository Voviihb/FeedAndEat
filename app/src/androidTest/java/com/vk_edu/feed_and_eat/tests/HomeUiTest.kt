package com.vk_edu.feed_and_eat.tests

import com.vk_edu.feed_and_eat.base.BaseUiTest
import com.vk_edu.feed_and_eat.screens.HomeScreen
import com.vk_edu.feed_and_eat.server.mocks.RecipesMockResponse
import com.vk_edu.feed_and_eat.server.mocks.UsersMockResponse
import com.vk_edu.feed_and_eat.server.success
import com.vk_edu.feed_and_eat.utils.waitForNodeWithTag
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import org.junit.Test


@HiltAndroidTest
class HomeUiTest : BaseUiTest() {

    private val homeDefaultResponses = listOf(
        RecipesMockResponse.dailyRecipeMock.success("home_test/recipes_daily_200.json"),
        RecipesMockResponse.topRecipesMock.success("home_test/recipes_list_200.json"),
        RecipesMockResponse.latestRecipesMock.success("home_test/recipes_list_200.json"),
        RecipesMockResponse.lowCalorieRecipesMock.success("home_test/recipes_list_200.json"),
        RecipesMockResponse.searchRecipesMock.success("home_test/recipes_list_200.json"),
        UsersMockResponse.myCollectionsMock.success("home_test/user_collections_empty_200.json"),
    )

    /**
     * Тест 1: Главный экран отображается сразу при запуске с пре-авторизованным пользователем.
     */
    @Test
    fun homeScreen_isDisplayed_withPreAuthorizedUser() = start(
        defaultResponses = homeDefaultResponses,
        isAuthorized = true,
    ) {
        // Ждём появления главного экрана
        waitForNodeWithTag(composeRule, "home_screen_content")

        // Проверяем строку поиска
        onComposeScreen<HomeScreen>(composeRule) {
            searchCard.assertIsDisplayed()
        }
    }
}
