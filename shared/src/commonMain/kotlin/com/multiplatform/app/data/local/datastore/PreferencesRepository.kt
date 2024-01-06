package com.multiplatform.app.data.local.datastore

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    val location: Flow<String>
    suspend fun saveLocation(location: String)

    suspend fun saveAccessToken(accessToken: String)
    val accessToken: Flow<String>
}
