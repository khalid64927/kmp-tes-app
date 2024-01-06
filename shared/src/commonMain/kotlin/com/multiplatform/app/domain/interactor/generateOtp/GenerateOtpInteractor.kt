package com.multiplatform.app.domain.interactor.generateOtp

import com.multiplatform.app.data.remote.models.Dto.GenerateOtpResponse
import com.multiplatform.app.domain.models.ResourceResult
import kotlinx.coroutines.flow.Flow

interface GenerateOtpInteractor {
    suspend operator fun invoke(msisdn: String,xClientCode: String): Flow<ResourceResult<GenerateOtpResponse>>
}