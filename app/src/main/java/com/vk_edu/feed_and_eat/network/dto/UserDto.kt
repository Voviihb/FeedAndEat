package com.vk_edu.feed_and_eat.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    val email: String,
    val username: String,
    @SerialName("is_active") val isActive: Boolean,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("about_me") val aboutMe: String? = null,
    @SerialName("is_profile_private") val isProfilePrivate: Boolean = false,
    @SerialName("theme_settings") val themeSettings: String = "light"
)

@Serializable
data class ProfileUpdateDto(
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("about_me") val aboutMe: String? = null,
    @SerialName("is_profile_private") val isProfilePrivate: Boolean? = null,
    @SerialName("theme_settings") val themeSettings: String? = null
)
