package com.iolandarosa.retailhub.core.datastore.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import com.iolandarosa.retailhub.core.datastore.utils.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

internal class TokenManagerImpl(private val dataStore: DataStore<Preferences>): TokenManager {
    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey(PreferencesKeys.ACCESS_TOKEN)
        val REFRESH_TOKEN = stringPreferencesKey(PreferencesKeys.REFRESH_TOKEN)
    }

    override fun getAccessToken(): Flow<String?> = dataStore.data
        .catch { emptyPreferences() }
        .map { it[ACCESS_TOKEN] }

    override suspend fun saveAuthTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }
}