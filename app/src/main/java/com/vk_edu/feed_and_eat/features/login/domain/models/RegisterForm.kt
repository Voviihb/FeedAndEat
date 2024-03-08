package com.vk_edu.feed_and_eat.features.login.domain.models

data class RegisterForm(
    val email: String = "",
    val login: String = "",
    val password1: String = "",
    val password2: String = ""
)
