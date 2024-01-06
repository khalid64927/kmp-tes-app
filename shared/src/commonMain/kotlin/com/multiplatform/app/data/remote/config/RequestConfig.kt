package com.multiplatform.app.data.remote.config

data class RequestConfig<T>(
    val urlPath: String,
    val resource: Any? = null,
    val postBodyJson: T? = null,
    val headerMap: Map<String, String> = emptyMap(),
    val postBody: Map<String, String> = emptyMap(),
)