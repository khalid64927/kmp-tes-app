package com.multiplatform.app.data.remote.requests

import kotlinx.serialization.SerialName

data class SubmitOtpRequest(
    @SerialName("challengeToken")
    val challengeToken: String,
    @SerialName("code")
    val code: String
)

data class SubmitOtpData(
    val challengeToken: String,
    val code: String,
    val xClientCode: String,
)
