package com.multiplatform.app.di

import com.multiplatform.app.data.remote.config.productionPinsMap
import com.multiplatform.app.data.remote.config.uatPinsMap
import com.multiplatform.app.BuildKonfig
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.engine.darwin.certificates.CertificatePinner


internal fun getEngine() = Darwin.create {
        val pinMap = if (BuildKonfig.FLAVOR == "uat") uatPinsMap else productionPinsMap
        val builder = CertificatePinner.Builder()
        pinMap.forEach { pinMapItem ->
            val host = pinMapItem.key
            val pins = pinMapItem.value.toTypedArray()
            builder.add(host, *pins)
        }
        handleChallenge(builder.build())
    }



