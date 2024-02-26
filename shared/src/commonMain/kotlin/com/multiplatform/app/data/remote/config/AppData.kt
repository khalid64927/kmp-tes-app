package com.multiplatform.app.data.remote.config

object AppData {
    var accessToken: String = ""
    var timeOutInMillis: Long = 10 * 60
    var paymentToken: String = ""
    var correlationId: String = ""
    var environment: Environment = Environment.UAT1
    var paymentUrl: String = ""
}

enum class Environment {
    MOCK, UAT1, UAT3, PRODUCTION
}