package com.multiplatform.app.domain.interactor.payNow

import com.multiplatform.app.data.remote.config.AppData
import com.multiplatform.app.data.remote.config.RequestConfig
import com.multiplatform.app.data.remote.config.onFailure
import com.multiplatform.app.data.remote.config.onSuccess
import com.multiplatform.app.data.remote.repositories.PrepaidRepository
import com.multiplatform.app.data.remote.requests.GetPayNowDataRequest
import com.multiplatform.app.domain.models.ResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPayNowInteractorImpl(
    private val appData: AppData,
    private val repository: PrepaidRepository
): GetPayNowInteractor {

    // TODO
    override suspend fun invoke(): Flow<ResourceResult<String>> = flow {
        emit(ResourceResult.Loading)
        repository.getPayNowData(
            RequestConfig(
                urlPath = "/api/sg/v2/auth/mfa/challenge/sms/otp",
                resource = GetPayNowDataRequest(""),
                headerMap = emptyMap()
                )
            ).onSuccess {
                emit(ResourceResult.Success(it))
            }.onFailure {
                emit(ResourceResult.Error(it))

        }
    }
}