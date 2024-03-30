package com.vk_edu.feed_and_eat

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(key: String, defaultValue: String? = null): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    fun removeData(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    companion object {
        const val PROJECT_PREFS = "FeedAndEatPrefs"
        const val CURRENT_USER = "currentUser"
    }
}