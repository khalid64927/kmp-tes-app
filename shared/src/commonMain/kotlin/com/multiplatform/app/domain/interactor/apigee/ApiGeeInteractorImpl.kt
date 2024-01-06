package com.multiplatform.app.domain.interactor.apigee

import com.multiplatform.app.data.remote.config.AppData
import com.multiplatform.app.data.remote.config.RequestConfig
import com.multiplatform.app.data.remote.config.onSuccess
import com.multiplatform.app.data.remote.models.Dto.AuthenticateResponse
import com.multiplatform.app.data.remote.repositories.PrepaidRepository
import com.multiplatform.app.BuildKonfig
import com.multiplatform.app.data.local.datastore.PreferencesRepository
import com.multiplatform.app.domain.models.ResourceResult
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.multiplatform.app.data.remote.config.onFailure
import com.multiplatform.app.data.remote.requests.AuthenticateRequest


class ApiGeeInteractorImpl(
    private val appData: AppData,
    private val preferencesRepository: PreferencesRepository,
    private val repository: PrepaidRepository): ApiGeeInteractor {
    override suspend fun invoke(): Flow<ResourceResult<AuthenticateResponse>> = flow {
        val clientId = BuildKonfig.PREPAID_CLIENT_ID
        val clientSecret = BuildKonfig.PREPAID_CLIENT_SECRET
        val hardcodedAuth = "Basic SkJFTHh5a2dNR0FqSlZ3SGpmcVRBaTRXV2V3YmdjTU86R2g2MzUwMVA1VHFCR3VadQ=="
        println("ApiGeeInteractorImpl")
        val configuration = RequestConfig<AuthenticateRequest>(
            headerMap = mapOf(HttpHeaders.Authorization to hardcodedAuth),
            urlPath = "/api/sg/v1/oauth/token",
        )
        repository.authenticate(configuration).
        onSuccess {
            println("onSuccess $it")
            appData.accessToken = it.accessToken
            println("onSuccess $it")
            emit(ResourceResult.Success(it))
        }.onFailure {
            emit(ResourceResult.Error(it))
        }

    }
}