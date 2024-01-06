package com.multiplatform.app.data.local.datastore

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStorePreferencesRepository(
    private val dataStoreProvider: DataStoreProvider,
) : PreferencesRepository {

    private val keyLocation = stringPreferencesKey("selected-location")
    private val accessTokenKey = stringPreferencesKey("accesstoken")

    override val location: Flow<String> =
        dataStoreProvider.dataStore.data.map { preferences -> preferences[keyLocation].orEmpty() }

    override suspend fun saveLocation(location: String) {
        dataStoreProvider.dataStore.edit { preferences ->
            preferences[keyLocation] = location
        }
    }

    override val accessToken: Flow<String> =
        dataStoreProvider.dataStore.data.map { preferences -> preferences[accessTokenKey].orEmpty() }
    override suspend fun saveAccessToken(accessToken: String) {
        dataStoreProvider.dataStore.edit { preferences ->
            preferences[accessTokenKey] = accessToken
        }
    }


}
