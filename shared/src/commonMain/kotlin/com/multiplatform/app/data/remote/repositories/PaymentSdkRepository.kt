package com.multiplatform.app.data.remote.repositories

import com.multiplatform.app.data.remote.config.RequestConfig

import com.multiplatform.app.data.remote.config.Result
import com.multiplatform.app.data.remote.models.dto.AuthenticateResponse
import com.multiplatform.app.data.remote.models.dto.GenerateOtpResponse
import com.multiplatform.app.data.remote.models.dto.InstrumentInfoResponse
import com.multiplatform.app.data.remote.models.dto.RegisterDeviceResponse
import com.multiplatform.app.data.remote.models.dto.RemoveCardResponse
import com.multiplatform.app.data.remote.models.dto.SubmitOtpResponse
import com.multiplatform.app.data.remote.requests.AuthenticateRequest
import com.multiplatform.app.data.remote.requests.GenerateOtpRequest
import com.multiplatform.app.data.remote.requests.GetPayNowDataRequest
import com.multiplatform.app.data.remote.requests.InstrumentInfoRequest
import com.multiplatform.app.data.remote.requests.RegisterDeviceRequest
import com.multiplatform.app.data.remote.requests.RemoveCardRequest
import com.multiplatform.app.data.remote.requests.SubmitOtpRequest

interface PaymentSdkRepository {
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

    suspend fun instrumentInfo(
        config: RequestConfig<InstrumentInfoRequest>
    ): Result<InstrumentInfoResponse>

    suspend fun removeCard(config: RequestConfig<RemoveCardRequest>): Result<RemoveCardResponse>

    // TODO
    suspend fun getPayNowData(config: RequestConfig<GetPayNowDataRequest>): Result<String>

}

fun defaultMap() = emptyMap<String, String>()
fun addMap(headerMap: Map<String, String>) {
    val mutableMap = mutableMapOf<String, String>()
    mutableMap.putAll(headerMap)
}

