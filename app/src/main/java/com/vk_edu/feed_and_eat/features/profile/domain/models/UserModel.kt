package com.vk_edu.feed_and_eat.features.profile.domain.models

import com.vk_edu.feed_and_eat.features.collection.domain.models.Compilation

data class UserModel(
    val userId: String,
    val avatarUrl: String?,
    val aboutMeData: String?,
    val collections: Compilation?,
    @field:JvmField
    val isProfilePrivate: Boolean = false,
    val themeSettings: String = "light"
)
