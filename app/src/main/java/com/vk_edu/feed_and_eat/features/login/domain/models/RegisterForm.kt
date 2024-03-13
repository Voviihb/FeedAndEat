package com.vk_edu.feed_and_eat.features.login.domain.models

data class RegisterForm(
    val email: String = "",
    val login: String = "",
    val password: String = "",
    val passwordControl: String = ""
)
