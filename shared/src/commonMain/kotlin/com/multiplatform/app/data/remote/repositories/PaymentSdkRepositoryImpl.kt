package com.multiplatform.app.data.remote.repositories

import com.multiplatform.app.data.remote.config.PrepaidApiException
import com.multiplatform.app.data.remote.config.RequestConfig
import com.multiplatform.app.data.remote.models.dto.AuthenticateResponse
import com.multiplatform.app.data.remote.models.dto.GenerateOtpResponse
import com.multiplatform.app.data.remote.models.dto.RegisterDeviceResponse
import com.multiplatform.app.data.remote.models.dto.SubmitOtpResponse
import com.multiplatform.app.data.remote.requests.AuthenticateRequest
import com.multiplatform.app.data.remote.requests.GenerateOtpRequest
import com.multiplatform.app.data.remote.requests.RegisterDeviceRequest
import com.multiplatform.app.data.remote.requests.SubmitOtpRequest
import com.multiplatform.app.data.local.datastore.PreferencesRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import com.multiplatform.app.data.remote.config.Result.Success
import com.multiplatform.app.data.remote.config.Result.Failure
import com.multiplatform.app.data.remote.config.Result
import com.multiplatform.app.data.remote.models.dto.ErrorDto
import com.multiplatform.app.data.remote.models.dto.InstrumentInfoResponse
import com.multiplatform.app.data.remote.models.dto.RemoveCardResponse
import com.multiplatform.app.data.remote.requests.GetPayNowDataRequest
import com.multiplatform.app.data.remote.requests.InstrumentInfoRequest
import com.multiplatform.app.data.remote.requests.RemoveCardRequest
import com.multiplatform.app.util.toJson
import io.ktor.client.statement.HttpResponse
import io.ktor.http.append
import io.ktor.http.parameters
import kotlinx.serialization.json.Json

class PaymentSdkRepositoryImpl(
    private val httpClient: HttpClient,
    private val authClient: HttpClient,
    private val preferencesRepository: PreferencesRepository
    ): PaymentSdkRepository {


    @OptIn(InternalAPI::class)
    override suspend fun authenticate(config: RequestConfig<AuthenticateRequest>): Result<AuthenticateResponse> {
        return authClient.safeRequest {
            contentType(ContentType.Application.FormUrlEncoded)
            method = HttpMethod.Post
            url {
                appendPathSegments("/api/sg/v1/oauth/token")
            }
            headers {
                for ((key, value) in config.headerMap){
                    append(key, value)
                }
                append(HttpHeaders.ContentType, "application/x-www-form-urlencoded")
            }
            body = TextContent("grant_type=client_credentials",ContentType.Application.FormUrlEncoded )
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun registerDevice(
        config: RequestConfig<RegisterDeviceRequest>,
    ): Result<RegisterDeviceResponse> {
        println("registerDevice api call")
        return httpClient.safeRequest {
            method = HttpMethod.Post
            url {
                appendPathSegments(config.urlPath)
            }
            // set body
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json)
            }
            config.postBodyJson?.run {
                body = this.toJson()
            }

        }
    }


    override suspend fun generateOtp(
        config: RequestConfig<GenerateOtpRequest>,
    ): Result<GenerateOtpResponse> {
        println("generateOtp api call")
        return httpClient.safeRequest {
            method = HttpMethod.Post
            url {
                appendPathSegments(config.urlPath)
            }
            contentType(ContentType.Application.Json)
            config.run {
                resource?.run {
                    setBody(this)
                }
            }
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json)
                for ((key, value) in config.headerMap){
                    append(key, value)
                }
            }

        }
    }

    override suspend fun submitOtp(config: RequestConfig<SubmitOtpRequest>,
    ): Result<SubmitOtpResponse> {

        return httpClient.safeRequest {
            url {
                appendPathSegments(config.urlPath)
            }
            config.run {
                resource?.run {
                    setBody(this)
                }
            }
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json)
                for ((key, value) in config.headerMap){
                    append(key, value)
                }
            }
        }
    }

    override suspend fun instrumentInfo(config: RequestConfig<InstrumentInfoRequest>): Result<InstrumentInfoResponse> {
        return httpClient.safeRequest {
            method = HttpMethod.Get
            url {
                appendPathSegments(config.urlPath)
            }
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json)
                for ((key, value) in config.headerMap){
                    append(key, value)
                }
            }
            parameters {
                for ((key, value) in config.parameters){
                    append(key, value)
                }
            }

        }
    }

    override suspend fun removeCard(config: RequestConfig<RemoveCardRequest>): Result<RemoveCardResponse> {
        return httpClient.safeRequest {
            method = HttpMethod.Post
            url {
                appendPathSegments(config.urlPath)
            }
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json)
                for ((key, value) in config.headerMap){
                    append(key, value)
                }
            }
            parameters {
                for ((key, value) in config.parameters){
                    append(key, value)
                }
            }

        }
    }

    override suspend fun getPayNowData(config: RequestConfig<GetPayNowDataRequest>): Result<String> {
        // TODO
        return Result.Success("")
    }
}

suspend inline fun <reified T> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): Result<T> {
    var httpResponse: HttpResponse? = null
    var result: Result<T> = Failure(Exception("Something went wrong !"))
    runCatching {
        httpResponse = request { block() }
        println("httpResponse is initialised")
        val response = httpResponse?.body() as T
        println("safeRequest end $response")
        result = Success(response)
    }.onFailure {
        println("safeReqsuest request completed")
        var errorDto = httpResponse?.getErrorDto()
        result = Failure(PrepaidApiException(
            errorMessage = it.message,
            error = it,
            responseBody = errorDto
        ))
    }
    return  result
}

suspend fun HttpResponse.getErrorDto(): ErrorDto? {
    var errorDto: ErrorDto? = null
    runCatching {
        errorDto = Json.decodeFromString(this.body())
    }
    return errorDto

}

