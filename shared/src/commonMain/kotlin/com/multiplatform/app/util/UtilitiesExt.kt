package com.multiplatform.app.util

import com.multiplatform.app.BuildKonfig
import io.ktor.util.encodeBase64
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


fun getBasicAuth(): String{
    val clientId = BuildKonfig.PREPAID_CLIENT_ID
    val clientSecret = BuildKonfig.PREPAID_CLIENT_SECRET
    val base64Credential = "$clientId:$clientSecret".encodeBase64()
    return "Basic $base64Credential"

}


inline fun <reified T : Any> T.toJson(): String {
    val json = Json { encodeDefaults = true }
    return json.encodeToString(this)
}