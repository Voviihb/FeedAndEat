package com.vk_edu.feed_and_eat.features.profile.pres

enum class ProfileType {
    PUBLIC, PRIVATE
}

enum class ThemeSelection {
    LIGHT, DARK, AS_SYSTEM
}

data class Settings(val themeSelection: ThemeSelection ,val profileType: ProfileType)
