package com.multiplatform.app.domain.interactor.payNow

import com.multiplatform.app.domain.models.ResourceResult
import kotlinx.coroutines.flow.Flow

interface GetPayNowInteractor {
    suspend operator fun invoke(): Flow<ResourceResult<String>>
}