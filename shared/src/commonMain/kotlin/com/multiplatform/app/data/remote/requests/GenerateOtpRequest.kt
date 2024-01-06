package com.multiplatform.app.data.remote.requests

import io.ktor.resources.Resource
import kotlinx.serialization.SerialName

@Resource("/api/sg/v2/auth/mfa/challenge/sms/otp")
data class GenerateOtpRequest(
    @SerialName("msisdn")
    val msisdn: String,
    @SerialName("xClientCode")
    val xClientCode: String,
)
