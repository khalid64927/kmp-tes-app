package com.multiplatform.app.data.remote.requests

data class RemoveCardRequest(
    val paymentToken: String,
    val correlationId: String)
