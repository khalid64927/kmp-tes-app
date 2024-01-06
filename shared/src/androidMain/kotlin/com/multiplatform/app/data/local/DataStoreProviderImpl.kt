package com.multiplatform.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.multiplatform.app.common.createDataStore
import com.multiplatform.app.common.dataStoreFileName
import com.multiplatform.app.data.local.datastore.DataStoreProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class DataStoreProviderImpl(
    context: Context,
): DataStoreProvider {
    override val dataStore: DataStore<Preferences> = createDataStore(
        coroutineScope = CoroutineScope(Job() + Dispatchers.IO),
        producePath = {
            context.filesDir.resolve(dataStoreFileName).absolutePath
        }
    )
}
