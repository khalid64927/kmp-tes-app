package com.multiplatform.app.data.remote.models.Dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticateResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("client_id")
    val clientId: String,
    @SerialName("expires_in")
    val expiresIn: String,
    @SerialName("issued_at")
    val issuedAt: String,
    @SerialName("refresh_count")
    val refreshCount: String,
    @SerialName("refresh_token_expires_in")
    val refreshTokenExpiresIn: String,
    @SerialName("scope")
    val scope: String,
    @SerialName("status")
    val status: String,
    @SerialName("token_type")
    val tokenType: String
)