package com.vk_edu.feed_and_eat.tests

import com.vk_edu.feed_and_eat.base.BaseUiTest
import com.vk_edu.feed_and_eat.screens.LoginScreen
import com.vk_edu.feed_and_eat.server.mocks.AuthMockResponse
import com.vk_edu.feed_and_eat.server.mocks.RecipesMockResponse
import com.vk_edu.feed_and_eat.server.mocks.UsersMockResponse
import com.vk_edu.feed_and_eat.server.response
import com.vk_edu.feed_and_eat.server.success
import com.vk_edu.feed_and_eat.utils.waitForNodeWithTag
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import org.junit.Test

@HiltAndroidTest
class LoginUiTest : BaseUiTest() {

    /**
     * Тест 1: Экран логина отображает все необходимые поля при запуске.
     */
    @Test
    fun loginScreen_fieldsAreDisplayed() = start {
        onComposeScreen<LoginScreen>(composeRule) {
            emailField.assertIsDisplayed()
            passwordField.assertIsDisplayed()
            loginButton.assertIsDisplayed()
        }
    }

    /**
     * Тест 2: При неверных данных (сервер возвращает 401) отображается ошибка.
     * После смены мока на 200  и повторного входа — происходит переход на главный экран.
     */
    @Test
    fun loginWithInvalidCredentials_showsError_thenSuccessAfterMockChange() = start(
        defaultResponses = listOf(
            AuthMockResponse.loginMock.response(401, "login_test/auth_token_401.json"),
            RecipesMockResponse.dailyRecipeMock.success("home_test/recipes_daily_200.json"),
            RecipesMockResponse.topRecipesMock.success("home_test/recipes_list_200.json"),
            RecipesMockResponse.latestRecipesMock.success("home_test/recipes_list_200.json"),
            RecipesMockResponse.lowCalorieRecipesMock.success("home_test/recipes_list_200.json"),
            RecipesMockResponse.searchRecipesMock.success("home_test/recipes_list_200.json"),
            UsersMockResponse.myCollectionsMock.success("home_test/user_collections_empty_200.json"),
        )
    ) {
        onComposeScreen<LoginScreen>(composeRule) {
            emailField.performTextInput("user@example.com")
            passwordField.performTextInput("wrongpassword")
            loginButton.performClick()
        }

        // Ждём появления ноды с ошибкой
        waitForNodeWithTag(composeRule, "login_error_text", useUnmergedTree = true)

        // Убеждаемся что ошибка видна
        onComposeScreen<LoginScreen>(composeRule) {
            errorText.assertIsDisplayed()
        }

        // Меняем мок: теперь /auth/token вернёт 200
        setMocks(AuthMockResponse.loginMock.success("login_test/auth_token_200.json"))

        // Повторяем вход с теми же данными
        onComposeScreen<LoginScreen>(composeRule) {
            loginButton.performClick()
        }

        // После успешного входа ждём навигации на HomeScreen
        waitForNodeWithTag(composeRule, "home_screen_content")
    }
}
