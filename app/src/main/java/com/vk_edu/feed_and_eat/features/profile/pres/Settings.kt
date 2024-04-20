package com.vk_edu.feed_and_eat.features.profile.pres

enum class ProfileType(val type: Boolean) {
    PUBLIC(false), PRIVATE(true)
}

enum class ThemeSelection(val themeName: String) {
    LIGHT("light"), DARK("dark"), AS_SYSTEM("system")
}
