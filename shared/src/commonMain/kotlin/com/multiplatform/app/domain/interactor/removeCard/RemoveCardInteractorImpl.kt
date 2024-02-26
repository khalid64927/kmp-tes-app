package com.multiplatform.app.domain.interactor.removeCard

import com.multiplatform.app.data.remote.models.dto.RemoveCardResponse
import com.multiplatform.app.data.remote.repositories.PaymentSdkRepository
import com.multiplatform.app.data.remote.requests.RemoveCardRequest
import com.multiplatform.app.domain.models.ResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoveCardInteractorImpl(repository: PaymentSdkRepository): RemoveCardInteractor {

    override suspend fun invoke(request: RemoveCardRequest): Flow<ResourceResult<RemoveCardResponse>> = flow {
        emit(ResourceResult.Success(RemoveCardResponse("")))
    }
}