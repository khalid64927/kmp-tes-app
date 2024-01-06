package com.multiplatform.app.data.remote.models.Dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerateOtpResponse(
    @SerialName("challengeToken")
    val challengeToken: String,
    @SerialName("retryAfter")
    val retryAfter: Int
)