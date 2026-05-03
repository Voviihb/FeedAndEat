package com.vk_edu.feed_and_eat.screenshot.login

import org.junit.Test
import com.vk_edu.feed_and_eat.features.login.pres.PasswordDiffersException
import com.vk_edu.feed_and_eat.features.login.pres.RegisterScreenContent
import com.vk_edu.feed_and_eat.screenshot.BaseScreenshotTest

class RegisterScreenshotTest : BaseScreenshotTest() {

    @Test
    fun registerScreen_emptyState() {
        snapshot("register_screen_empty") {
            RegisterScreenContent(
                email = "",
                login = "",
                password = "",
                passwordControl = "",
                loading = false,
                error = null,
                onEmailChange = {},
                onLoginChange = {},
                onPassword1Change = {},
                onPassword2Change = {},
                onSignUpClick = {},
                onLoginClick = {},
            )
        }
    }

    @Test
    fun registerScreen_filledFields() {
        snapshot("register_screen_filled") {
            RegisterScreenContent(
                email = "newuser@example.com",
                login = "chef_master",
                password = "pass1234",
                passwordControl = "pass1234",
                loading = false,
                error = null,
                onEmailChange = {},
                onLoginChange = {},
                onPassword1Change = {},
                onPassword2Change = {},
                onSignUpClick = {},
                onLoginClick = {},
            )
        }
    }

    @Test
    fun registerScreen_withPasswordError() {
        snapshot("register_screen_password_error") {
            RegisterScreenContent(
                email = "newuser@example.com",
                login = "chef_master",
                password = "",
                passwordControl = "",
                loading = false,
                error = PasswordDiffersException(),
                onEmailChange = {},
                onLoginChange = {},
                onPassword1Change = {},
                onPassword2Change = {},
                onSignUpClick = {},
                onLoginClick = {},
            )
        }
    }

    @Test
    fun registerScreen_loading() {
        snapshot("register_screen_loading") {
            RegisterScreenContent(
                email = "newuser@example.com",
                login = "chef_master",
                password = "pass1234",
                passwordControl = "pass1234",
                loading = true,
                error = null,
                onEmailChange = {},
                onLoginChange = {},
                onPassword1Change = {},
                onPassword2Change = {},
                onSignUpClick = {},
                onLoginClick = {},
            )
        }
    }
}
