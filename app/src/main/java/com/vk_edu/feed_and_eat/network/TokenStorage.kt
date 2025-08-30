package com.vk_edu.feed_and_eat.network

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "auth_tokens")

@Singleton
class TokenStorage @Inject constructor(@ApplicationContext private val context: Context) {
    private val ACCESS = stringPreferencesKey("access_token")
    private val REFRESH = stringPreferencesKey("refresh_token")

    val accessToken: Flow<String?> = context.dataStore.data.map { it[ACCESS] }
    val refreshToken: Flow<String?> = context.dataStore.data.map { it[REFRESH] }

    suspend fun saveTokens(access: String, refresh: String) {
        context.dataStore.edit {
            it[ACCESS] = access
            it[REFRESH] = refresh
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
