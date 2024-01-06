package com.multiplatform.app.domain.interactor.apigee

import com.multiplatform.app.data.remote.models.Dto.AuthenticateResponse
import com.multiplatform.app.domain.models.ResourceResult
import kotlinx.coroutines.flow.Flow

interface ApiGeeInteractor {
    suspend operator fun invoke(): Flow<ResourceResult<AuthenticateResponse>>
}