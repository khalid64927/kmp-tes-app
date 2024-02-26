package com.multiplatform.app.data.remote.requests

import io.ktor.resources.Resource
import kotlinx.serialization.SerialName

@Resource("/api/sg/v1/pp-payment-ex/instrInfo")
data class InstrumentInfoRequest(
    @SerialName("paymentToken")
    val paymentToken: String,
    @SerialName("correlationId")
    val correlationId: String)
