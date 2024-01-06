package com.multiplatform.app.data.remote.models.Dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterDeviceResponse(
    @SerialName("id")
    val id: String,
    @SerialName("key")
    val key: String
)