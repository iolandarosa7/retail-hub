/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.datastore.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.iolandarosa.retailhub.core.datastore.domain.TokenManager
import com.iolandarosa.retailhub.core.datastore.utils.PreferencesKeys
import com.iolandarosa.retailhub.core.model.AuthTokens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

internal class TokenManagerImpl(
    private val dataStore: DataStore<Preferences>,
) : TokenManager {
    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey(PreferencesKeys.ACCESS_TOKEN)
        val REFRESH_TOKEN = stringPreferencesKey(PreferencesKeys.REFRESH_TOKEN)
    }

    override fun getAuthTokens(): Flow<AuthTokens?> =
        dataStore.data
            .catch { emptyPreferences() }
            .map {
                val accessToken = it[ACCESS_TOKEN] ?: return@map null
                val refreshToken = it[REFRESH_TOKEN] ?: return@map null
                AuthTokens(accessToken, refreshToken)
            }

    override suspend fun saveAuthTokens(
        accessToken: String,
        refreshToken: String,
    ) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
        }
    }
}
