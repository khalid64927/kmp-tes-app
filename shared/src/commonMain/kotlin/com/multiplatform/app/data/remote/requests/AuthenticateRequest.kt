package com.multiplatform.app.data.remote.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticateRequest(
    @SerialName("grant_type")
    val grant_type: String = "client_credentials")