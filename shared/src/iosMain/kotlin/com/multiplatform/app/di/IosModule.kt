package com.multiplatform.app.di

import com.multiplatform.app.data.local.DataStoreProviderImpl
import com.multiplatform.app.data.local.datastore.DataStoreProvider
import com.multiplatform.app.domain.location.IosLocationService
import com.multiplatform.app.domain.location.LocationService
import com.multiplatform.app.database.AppDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import dev.icerock.moko.permissions.ios.PermissionsController
import dev.icerock.moko.permissions.ios.PermissionsControllerProtocol
import org.koin.dsl.module

val appModule = module {
    single<DataStoreProvider> { DataStoreProviderImpl() }
    single { getEngine() }
    single<SqlDriver> {
        NativeSqliteDriver(AppDatabase.Schema, "location.db")
    }
    single<PermissionsControllerProtocol> { PermissionsController() }
    single<LocationService> { IosLocationService() }
}
