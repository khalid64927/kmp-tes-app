package com.multiplatform.app.domain.interactor.instrumentInfo

import com.multiplatform.app.data.remote.models.dto.InstrumentInfoResponse
import com.multiplatform.app.data.remote.requests.InstrumentInfoRequest
import com.multiplatform.app.domain.models.ResourceResult
import kotlinx.coroutines.flow.Flow

interface GetInstrumentInfoInteractor {
    suspend operator fun invoke(
        request: InstrumentInfoRequest): Flow<ResourceResult<InstrumentInfoResponse>>
}