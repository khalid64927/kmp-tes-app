package com.multiplatform.app.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.platformLogWriter
import com.multiplatform.app.database.AppDatabase
import com.multiplatform.app.data.remote.config.AuthClientConfig
import com.multiplatform.app.data.remote.config.PrepaidClientConfig
import com.multiplatform.app.data.remote.repositories.PrepaidRepository
import com.multiplatform.app.data.remote.repositories.PrepaidRepositoryImpl
import com.multiplatform.app.domain.interactor.apigee.ApiGeeInteractor
import com.multiplatform.app.domain.interactor.apigee.ApiGeeInteractorImpl
import com.multiplatform.app.domain.interactor.generateOtp.GenerateOtpInteractor
import com.multiplatform.app.domain.interactor.generateOtp.GenerateOtpInteractorImpl
import com.multiplatform.app.domain.interactor.registerDevice.RegisterDeviceInteractor
import com.multiplatform.app.domain.interactor.registerDevice.RegisterDeviceInteractorImpl
import com.multiplatform.app.domain.interactor.submitOtp.SubmitOtpInteractor
import com.multiplatform.app.domain.interactor.submitOtp.SubmitOtpIteractorImpl
import com.multiplatform.app.data.local.datastore.DataStorePreferencesRepository
import com.multiplatform.app.data.local.datastore.PreferencesRepository
import com.multiplatform.app.data.local.db.LocationDataSource
import com.multiplatform.app.data.local.db.SqDelightLocationDataSource
import io.ktor.client.HttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module

val prepaidDataModule = module {

    factory { (tag: String? ) -> if (tag != null) baseLogger.withTag(tag) else baseLogger }
    single<PreferencesRepository> { DataStorePreferencesRepository(dataStoreProvider = get()) }
    single<LocationDataSource> {
        SqDelightLocationDataSource(
            database = AppDatabase(driver = get())
        )
    }

    // This client is used authenticating with apigee
    single<HttpClient> (named("authClient")) {
        val client = AuthClientConfig()
        client.createApiGeeHttpClient(
            httpClientEngine = get(),
            preferencesRepository = get(),
            log = getWith<Logger>(
                "Prepaid-Ktor").
            withTag("Prepaid-Ktor-Client")
        )
    }

    // This client is used for all api connection
    single<HttpClient> (named("prepaidClient")) {
        val client = PrepaidClientConfig()
        client.createPrepaidHttpClient(
            httpClientEngine = get(),
            preferencesRepository = get(),
            log = getWith<Logger>(
                "Prepaid-Ktor").
            withTag("Prepaid-Ktor-Client")
        )
    }
    single<PrepaidRepository> {
        PrepaidRepositoryImpl(
            httpClient = get( qualifier = named("prepaidClient")),
            authClient = get( qualifier = named("authClient")),
            preferencesRepository = get()
        )
    }

    single { com.multiplatform.app.data.remote.config.AppData() }
}

val prepaidDomainModule = module {
    factory<ApiGeeInteractor> {
        ApiGeeInteractorImpl( preferencesRepository = get(), repository = get(), appData = get()) }
    factory<GenerateOtpInteractor> {
        GenerateOtpInteractorImpl(repository = get()) }
    factory<SubmitOtpInteractor> {
        SubmitOtpIteractorImpl( repository = get()) }
    factory<RegisterDeviceInteractor> {
        RegisterDeviceInteractorImpl(preferencesRepository = get(), repository = get()) }
}

// platformLogWriter() is a relatively simple config option, useful for local debugging. For production
// uses you *may* want to have a more robust configuration from the native platform. In KaMP Kit,
// that would likely go into platformModule expect/actual.
// See https://github.com/touchlab/Kermit
val baseLogger = Logger(config = StaticConfig(logWriterList = listOf(platformLogWriter())), "MyWeather")

internal inline fun <reified T> Scope.getWith(vararg params: Any?): T {
    return get(parameters = { parametersOf(*params) })
}

// Simple function to clean up the syntax a bit
fun KoinComponent.injectLogger(tag: String = "Ktor"): Lazy<Logger> = inject { parametersOf(tag) }