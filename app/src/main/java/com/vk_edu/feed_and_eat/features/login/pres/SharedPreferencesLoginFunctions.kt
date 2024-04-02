package com.vk_edu.feed_and_eat.features.login.pres

import com.vk_edu.feed_and_eat.PreferencesManager

fun writeUserId(preferencesManager: PreferencesManager, id: String) {
    preferencesManager.saveData(key = PreferencesManager.CURRENT_USER, id)
}

fun removeUserId(preferencesManager: PreferencesManager) {
    preferencesManager.removeData(key = PreferencesManager.CURRENT_USER)
}

fun getCurrentUserId(preferencesManager: PreferencesManager): String? =
    preferencesManager.getData(key = PreferencesManager.CURRENT_USER)
