package com.multiplatform.app.data.remote.config

import com.multiplatform.app.data.remote.models.Dto.ErrorDto


open class PrepaidApiException(
    open val errorMessage: String?,
    open val error: Throwable? = null,
    open val responseBody: ErrorDto? = null
) : Throwable(errorMessage, error)