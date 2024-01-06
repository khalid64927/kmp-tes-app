package com.multiplatform.app.domain.interactor.submitOtp

import com.multiplatform.app.data.remote.models.Dto.SubmitOtpResponse
import com.multiplatform.app.data.remote.requests.SubmitOtpData
import com.multiplatform.app.domain.models.ResourceResult
import kotlinx.coroutines.flow.Flow

interface SubmitOtpInteractor {
    suspend operator fun invoke(requestData: SubmitOtpData): Flow<ResourceResult<SubmitOtpResponse>>
}