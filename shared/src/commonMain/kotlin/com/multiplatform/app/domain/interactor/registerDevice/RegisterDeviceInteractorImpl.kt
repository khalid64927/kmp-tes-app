package com.multiplatform.app.domain.interactor.registerDevice

import com.multiplatform.app.data.remote.config.RequestConfig
import com.multiplatform.app.data.remote.config.onFailure
import com.multiplatform.app.data.remote.config.onSuccess
import com.multiplatform.app.data.remote.models.Dto.RegisterDeviceResponse
import com.multiplatform.app.data.remote.repositories.PrepaidRepository
import com.multiplatform.app.data.remote.requests.RegisterDeviceRequest
import com.multiplatform.app.data.local.datastore.PreferencesRepository
import com.multiplatform.app.domain.models.ResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegisterDeviceInteractorImpl(
    private val preferencesRepository: PreferencesRepository,
    private val repository: PrepaidRepository): RegisterDeviceInteractor {
    override suspend fun invoke(): Flow<ResourceResult<RegisterDeviceResponse>> = flow {
        emit(ResourceResult.Loading)
        repository.registerDevice(RequestConfig(
            urlPath = "api/sg/v1/auth/mfa/devices",
            postBodyJson = RegisterDeviceRequest(),
        )).onSuccess {
            println("registerDevice success")
            emit(ResourceResult.Success(it))
        }.onFailure {
            println("registerDevice onFailure")
            emit(ResourceResult.Error(it))
        }

    }


}