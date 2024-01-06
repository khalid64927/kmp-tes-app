package com.multiplatform.app.domain.interactor.generateOtp

import com.multiplatform.app.data.remote.config.RequestConfig
import com.multiplatform.app.data.remote.config.onFailure
import com.multiplatform.app.data.remote.config.onSuccess
import com.multiplatform.app.data.remote.models.Dto.GenerateOtpResponse
import com.multiplatform.app.data.remote.repositories.PrepaidRepository
import com.multiplatform.app.data.remote.requests.GenerateOtpRequest
import com.multiplatform.app.domain.models.ResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GenerateOtpInteractorImpl(val repository: PrepaidRepository): GenerateOtpInteractor {
    override suspend fun invoke(
        msisdn: String,
        xClientCode: String
    ): Flow<ResourceResult<GenerateOtpResponse>>  = flow {
        emit(ResourceResult.Loading)
        repository.generateOtp(
            RequestConfig<GenerateOtpRequest>(
                urlPath = "/api/sg/v2/auth/mfa/challenge/sms/otp",
                resource = GenerateOtpRequest(
                    msisdn = "65$msisdn",
                    xClientCode = xClientCode,),
                headerMap = mapOf("x-client-code" to xClientCode)

            )
        ).onSuccess {
            emit(ResourceResult.Success(it))

        }.onFailure {
            emit(ResourceResult.Error(it))

        }
    }

}