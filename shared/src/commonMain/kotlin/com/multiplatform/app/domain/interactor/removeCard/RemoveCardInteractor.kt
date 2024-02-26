package com.multiplatform.app.domain.interactor.removeCard

import com.multiplatform.app.data.remote.models.dto.RemoveCardResponse
import com.multiplatform.app.data.remote.requests.RemoveCardRequest
import com.multiplatform.app.domain.models.ResourceResult
import kotlinx.coroutines.flow.Flow

interface RemoveCardInteractor {
    suspend operator fun invoke(
        request: RemoveCardRequest
    ): Flow<ResourceResult<RemoveCardResponse>>


}