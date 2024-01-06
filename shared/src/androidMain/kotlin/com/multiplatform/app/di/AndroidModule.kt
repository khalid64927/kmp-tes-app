package com.multiplatform.app.di

import com.multiplatform.app.data.local.datastore.DataStoreProvider
import com.multiplatform.app.data.local.DataStoreProviderImpl
import com.multiplatform.app.database.AppDatabase
import com.multiplatform.app.domain.location.AndroidLocationService
import com.multiplatform.app.domain.location.LocationService
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dev.icerock.moko.permissions.PermissionsController
import io.ktor.client.engine.HttpClientEngine
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single<DataStoreProvider> { DataStoreProviderImpl(androidContext()) }

    single<HttpClientEngine> { getEngine() }
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = androidContext(),
            name = "location.db",
        )
    }
    single { PermissionsController(applicationContext = androidContext())}
    factory <LocationService> { AndroidLocationService(context = androidContext()) }
}
