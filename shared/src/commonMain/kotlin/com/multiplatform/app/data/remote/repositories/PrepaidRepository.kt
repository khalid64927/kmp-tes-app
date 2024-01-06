package com.multiplatform.app.data.remote.repositories

import com.multiplatform.app.data.remote.config.RequestConfig
import com.multiplatform.app.data.remote.models.Dto.AuthenticateResponse
import com.multiplatform.app.data.remote.models.Dto.GenerateOtpResponse
import com.multiplatform.app.data.remote.models.Dto.RegisterDeviceResponse
import com.multiplatform.app.data.remote.models.Dto.SubmitOtpResponse
import com.multiplatform.app.data.remote.config.Result
import com.multiplatform.app.data.remote.requests.AuthenticateRequest
import com.multiplatform.app.data.remote.requests.GenerateOtpRequest
import com.multiplatform.app.data.remote.requests.RegisterDeviceRequest
import com.multiplatform.app.data.remote.requests.SubmitOtpRequest

interface PrepaidRepository {
    suspend fun authenticate(
        config: RequestConfig<AuthenticateRequest>
    ): Result<AuthenticateResponse>
    suspend fun registerDevice(
        config: RequestConfig<RegisterDeviceRequest>
    ): Result<RegisterDeviceResponse>
    suspend fun generateOtp(
        config: RequestConfig<GenerateOtpRequest>
    ): Result<GenerateOtpResponse>
    suspend fun submitOtp(config: RequestConfig<SubmitOtpRequest>
    ): Result<SubmitOtpResponse>

}

fun defaultMap() = emptyMap<String, String>()
fun addMap(headerMap: Map<String, String>) {
    val mutableMap = mutableMapOf<String, String>()
    mutableMap.putAll(headerMap)
}

