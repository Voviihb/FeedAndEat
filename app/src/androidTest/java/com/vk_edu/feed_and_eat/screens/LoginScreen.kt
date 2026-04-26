package com.vk_edu.feed_and_eat.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class LoginScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<LoginScreen>(
        semanticsProvider = semanticsProvider,
        viewBuilderAction = { hasTestTag("login_screen") },
    ) {

    val emailField: KNode = child { hasTestTag("login_email_field") }

    val passwordField: KNode = child { hasTestTag("login_password_field") }

    val loginButton: KNode = child { hasTestTag("login_button") }

    val errorText: KNode = child {
        hasTestTag("login_error_text")
        useUnmergedTree = true
    }
}
