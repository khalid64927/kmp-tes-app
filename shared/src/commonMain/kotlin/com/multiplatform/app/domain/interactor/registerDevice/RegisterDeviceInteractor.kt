package com.multiplatform.app.domain.interactor.registerDevice

import com.multiplatform.app.data.remote.models.Dto.RegisterDeviceResponse
import com.multiplatform.app.domain.models.ResourceResult
import kotlinx.coroutines.flow.Flow

interface RegisterDeviceInteractor {
    suspend operator fun invoke(): Flow<ResourceResult<RegisterDeviceResponse>>
}