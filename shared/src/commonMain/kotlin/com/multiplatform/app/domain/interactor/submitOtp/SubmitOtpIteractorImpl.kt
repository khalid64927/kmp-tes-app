package com.multiplatform.app.domain.interactor.submitOtp

import com.multiplatform.app.data.remote.config.RequestConfig
import com.multiplatform.app.data.remote.config.onFailure
import com.multiplatform.app.data.remote.config.onSuccess
import com.multiplatform.app.data.remote.models.Dto.SubmitOtpResponse
import com.multiplatform.app.data.remote.repositories.PrepaidRepository
import com.multiplatform.app.data.remote.requests.SubmitOtpData
import com.multiplatform.app.data.remote.requests.SubmitOtpRequest
import com.multiplatform.app.domain.models.ResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SubmitOtpIteractorImpl(private val repository: PrepaidRepository): SubmitOtpInteractor {
    override suspend fun invoke(requestData: SubmitOtpData): Flow<ResourceResult<SubmitOtpResponse>>  = flow {
        emit(ResourceResult.Loading)
        repository.submitOtp(RequestConfig(
            urlPath = "/api/sg/v2/auth/mfa/auth/sms/otp",
            SubmitOtpRequest(
                challengeToken = requestData.challengeToken,
                code = requestData.code,
            ),
            headerMap = mapOf("x-client-code" to requestData.xClientCode)
        )).
        onSuccess { emit(ResourceResult.Success(it)) }.
        onFailure { emit(ResourceResult.Error(it))  }
    }
}