package com.multiplatform.app.data.remote.config

import com.multiplatform.app.data.remote.repositories.PrepaidRepository
import com.multiplatform.app.domain.interactor.apigee.ApiGeeInteractor
import com.multiplatform.app.BuildKonfig
import com.multiplatform.app.data.local.datastore.PreferencesRepository
import com.multiplatform.app.data.remote.models.RemoteException
import com.multiplatform.app.data.remote.models.RemoteExceptionType
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.EMPTY
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.statement.request
import io.ktor.http.URLProtocol
import io.ktor.serialization.JsonConvertException
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthClientConfig {
    fun createApiGeeHttpClient(
        httpClientEngine: HttpClientEngine,
        preferencesRepository: PreferencesRepository,
        log: co.touchlab.kermit.Logger,
    ) = HttpClient(httpClientEngine) {
        expectSuccess = true
        install(Resources)
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000 // Set the connection timeout in milliseconds
        }

        exceptionHandling()
        install(Logging) {
            level = LogLevel.ALL
            logger = if (BuildKonfig.FLAVOR.isNotEmpty()) {
                object : Logger {
                    override fun log(message: String) {
                        log.i {message}
                    }
                }
            } else {
                Logger.EMPTY
            }
        }
        defaultRequest {
            url {
                // TODO: change the host, below host is invalid
                host = "api.weatherapi.com"
                protocol = URLProtocol.HTTPS
                // TODO: Add api key
                //parameters.append("key", BuildKonfig.API_KEY)
            }

        }
    }

    private fun <T : HttpClientEngineConfig> HttpClientConfig<T>.exceptionHandling() {
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                val type = when (exception) {
                    is ClientRequestException -> RemoteExceptionType.CLIENT_ERROR
                    is ServerResponseException -> RemoteExceptionType.SERVER_ERROR
                    is JsonConvertException -> RemoteExceptionType.PARSING_ERROR
                    else -> RemoteExceptionType.UNKNOWN
                }
                throw RemoteException(type)
            }
        }
    }
}

val AuthPlugin = createClientPlugin(name = "AuthPlugin", ::AuthPluginConfig) {
    on(Send) { request ->
        if(pluginConfig.appData.accessToken.isNotEmpty()) {
            request.headers.append("Authorization", "Bearer ${pluginConfig.appData.accessToken}")
        }
        var originalCall = proceed(request)
        originalCall.response.request.url
        originalCall.response.run { // this: Htt
            println("response :: ${this.status.value}")
            when(status.value) {
                400, 401, 403 -> {
                    if(pluginConfig.appData.accessToken.isNotEmpty()){
                        println("accessToken is available")
                        return@on originalCall
                    }
                    // Handle Unauthorized (status code 401)
                    // Handle Forbidden (status code 403)
                    // TODO: add re-authentication logic here
                    originalCall
                }
                // Add more cases as needed
                else -> {
                    // Handle other status codes
                    originalCall
                }
            }
        }
        originalCall
    }
}


class AuthPluginConfig: KoinComponent {
    val appData: AppData by inject()
    val apiGeeInteractor: ApiGeeInteractor by inject()
    val repository: PrepaidRepository by inject()

}