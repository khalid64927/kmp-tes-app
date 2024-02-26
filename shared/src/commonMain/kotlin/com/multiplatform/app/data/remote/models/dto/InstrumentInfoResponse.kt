package com.multiplatform.app.data.remote.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InstrumentInfoResponse(
    @SerialName("amount")
    val amount: String,
    @SerialName("currency")
    val currency: String,
    @SerialName("failureRedirectUrl")
    val failureRedirectUrl: String,
    @SerialName("gstDetails")
    val gstDetails: String,
    @SerialName("instrumentInfo")
    val instrumentInfo: List<InstrumentInfo>,
    @SerialName("preferredPaymentMethod")
    val preferredPaymentMethod: String,
    @SerialName("successRedirectUrl")
    val successRedirectUrl: String,
    @SerialName("txnType")
    val txnType: String
)

@Serializable
data class InstrumentInfo(
    @SerialName("bankList")
    val bankList: List<Bank> = emptyList(),
    @SerialName("cardInfo")
    val cardInfo: List<CardInfo> = emptyList(),
    @SerialName("enabled")
    val enabled: Boolean,
    @SerialName("instrument")
    val instrument: String,
    @SerialName("message")
    val message: String,
    @SerialName("operationsAllowed")
    val operationsAllowed: List<String> = emptyList(),
    @SerialName("ordinal")
    val ordinal: Int,
    @SerialName("serviceAvailable")
    val serviceAvailable: Boolean = false,
    @SerialName("sessionJsUrl")
    val sessionJsUrl: String = ""
)

@Serializable
data class Bank(
    @SerialName("bankCode")
    val bankCode: String,
    @SerialName("bankName")
    val bankName: String
)

@Serializable
data class CardInfo(
    @SerialName("expired")
    val expired: Boolean,
    @SerialName("instrId")
    val instrId: String,
    @SerialName("instrToken")
    val instrToken: String,
    @SerialName("instrType")
    val instrType: String
)