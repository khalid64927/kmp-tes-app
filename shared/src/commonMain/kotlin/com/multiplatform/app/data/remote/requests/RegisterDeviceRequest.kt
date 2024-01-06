package com.multiplatform.app.data.remote.requests

import com.multiplatform.app.util.PlatformExpects
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO: Resource class is for URL building and not for post body


@Serializable
data class RegisterDeviceRequest(
    @SerialName("client")
    val client: String = "heya",
    @SerialName("name")
    val name: String = "heya",
    @SerialName("model")
    val model: String = PlatformExpects().getModelName(),
    @SerialName("manufacturer")
    val manufacturer: String = "apple",
    @SerialName("os")
    val os: String = "iOS"
)
