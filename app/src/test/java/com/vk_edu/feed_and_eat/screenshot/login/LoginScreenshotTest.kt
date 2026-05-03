package com.vk_edu.feed_and_eat.screenshot.login

import org.junit.Test
import com.vk_edu.feed_and_eat.features.login.pres.LoginScreenContent
import com.vk_edu.feed_and_eat.screenshot.BaseScreenshotTest

class LoginScreenshotTest : BaseScreenshotTest() {

    @Test
    fun loginScreen_emptyState() {
        snapshot("login_screen_empty") {
            LoginScreenContent(
                email = "",
                password = "",
                loading = false,
                error = null,
                onEmailChange = {},
                onPasswordChange = {},
                onLoginClick = {},
                onNoAuthClick = {},
                onRegisterClick = {},
            )
        }
    }

    @Test
    fun loginScreen_filledFields() {
        snapshot("login_screen_filled") {
            LoginScreenContent(
                email = "user@example.com",
                password = "secret123",
                loading = false,
                error = null,
                onEmailChange = {},
                onPasswordChange = {},
                onLoginClick = {},
                onNoAuthClick = {},
                onRegisterClick = {},
            )
        }
    }

    @Test
    fun loginScreen_withError() {
        snapshot("login_screen_error") {
            LoginScreenContent(
                email = "user@example.com",
                password = "",
                loading = false,
                error = Exception("Неверный логин или пароль"),
                onEmailChange = {},
                onPasswordChange = {},
                onLoginClick = {},
                onNoAuthClick = {},
                onRegisterClick = {},
            )
        }
    }

    @Test
    fun loginScreen_loading() {
        snapshot("login_screen_loading") {
            LoginScreenContent(
                email = "user@example.com",
                password = "secret123",
                loading = true,
                error = null,
                onEmailChange = {},
                onPasswordChange = {},
                onLoginClick = {},
                onNoAuthClick = {},
                onRegisterClick = {},
            )
        }
    }
}
