package com.multiplatform.app.domain.interactor.initPayment

import com.multiplatform.app.data.remote.models.dto.InstrumentInfoResponse
import com.multiplatform.app.data.remote.requests.InstrumentInfoRequest
import com.multiplatform.app.domain.models.ResourceResult
import kotlinx.coroutines.flow.Flow

interface InitiPaymentInteractor {
    suspend operator fun invoke(
        request: InstrumentInfoRequest): Flow<ResourceResult<InstrumentInfoResponse>>
}