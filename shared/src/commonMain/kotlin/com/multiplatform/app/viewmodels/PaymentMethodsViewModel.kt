package com.multiplatform.app.viewmodels

import co.touchlab.kermit.Logger
import com.multiplatform.app.MR
import com.multiplatform.app.common.BaseViewModel
import com.multiplatform.app.data.remote.config.AppData
import com.multiplatform.app.data.remote.models.dto.InstrumentInfo
import com.multiplatform.app.data.remote.models.dto.InstrumentInfoResponse
import com.multiplatform.app.data.remote.requests.InstrumentInfoRequest
import com.multiplatform.app.data.remote.requests.RemoveCardRequest
import com.multiplatform.app.di.baseLogger
import com.multiplatform.app.domain.interactor.initPayment.InitiPaymentInteractor
import com.multiplatform.app.domain.interactor.instrumentInfo.GetInstrumentInfoInteractor
import com.multiplatform.app.domain.interactor.removeCard.RemoveCardInteractor
import com.multiplatform.app.domain.models.ResourceResult
import dev.icerock.moko.resources.ImageResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import org.koin.core.component.get

class PaymentMethodsViewModel: BaseViewModel() {
    private val _state = MutableStateFlow(PaymentMethodsUiState())
    val state: StateFlow<PaymentMethodsUiState> = _state.asStateFlow()
    val getInstrumentInfoInteractor: GetInstrumentInfoInteractor = get()
    val removeCardInteractor: RemoveCardInteractor = get()
    val initiPaymentInteractor: InitiPaymentInteractor = get()
    val logger: Logger = baseLogger.withTag("PaymentMethodsViewModel")

    init {
        getInstrumentInfo()
    }

    private fun removeCard(instrument: String, instrumentId: String){
        launchAsync {
            removeCardInteractor.invoke(RemoveCardRequest("", "")).
            collectLatest {
                when(it){
                    is ResourceResult.Loading -> _state.loadingRemoveCardApi()
                    is ResourceResult.Error -> _state.failedRemoveCardApi(it.throwable)
                    is ResourceResult.Success -> _state.successRemoveCardApi(
                        instrument = instrument, instrumentId = instrumentId)
                    else -> {}
                }
            }
        }
    }

    private fun makePayment() {
        launchAsync {
            initiPaymentInteractor.invoke(InstrumentInfoRequest("", "")).
            collectLatest {
                when(it){
                    is ResourceResult.Loading -> _state.loadingInitPayApi()
                    is ResourceResult.Error -> _state.failedInstrInfo(it.throwable)
                    is ResourceResult.Success -> _state.successInitPayApi()
                    else -> {}
                }
            }
        }

    }

    private fun getInstrumentInfo(){
        launchAsync {
            getInstrumentInfoInteractor.invoke(
                InstrumentInfoRequest(AppData.paymentToken, AppData.correlationId)).
            collectLatest {
                when(it){
                    is ResourceResult.Loading -> _state.loadingInstrInfo()
                    is ResourceResult.Error -> _state.failedInstrInfo(it.throwable)
                    is ResourceResult.Success -> _state.successInstrInfo(it.data.toSheetData())
                    else -> {}
                }
            }
        }
    }

    private fun navigateToWebView(){
        TODO()
    }

    private fun paymentSelected(
        instrument: String,
        instrumentId: String,
        instrumentType: String){
        when(instrument) {
            "CC" -> {
                makePayment()
            }
            "ENETS", "EGIRO", "AMEX_CC", "BNPL" -> navigateToWebView()
            else -> {}
        }
        makePayment()
    }

    fun onEvent(event: PaymentMethodEvent){
        logger.d("event ${event::class.simpleName}")
        when(event){
            is PaymentMethodEvent.PaymentOptionSelected -> paymentSelected(
                instrument = event.instrument,
                instrumentId = event.instrumentId,
                instrumentType = event.instrumentType)
            is PaymentMethodEvent.RemoveSavedCard -> removeCard(instrument = event.instrument, instrumentId = event.intrumentId)
            is PaymentMethodEvent.CardRemovedSuccessfully ->  _state.successRemoveCardApi("", "")
            is PaymentMethodEvent.FailedToRemoveCard -> _state.failedRemoveCardApi(event.throwable)
            is PaymentMethodEvent.GetInstrumentData -> getInstrumentInfo()
            is PaymentMethodEvent.LogEvent -> logger.d(" ${event.message}")
        }
    }
}

data class SheetData(val title: String = "",
                     var hyperLinkText: String? = null,
                     val leftIconResource: ImageResource = MR.images.ic_paynow,
                     var rightImageResource: ImageResource = MR.images.ic_right_arrow,
                     val serviceAvailable: Boolean = false,
                     val instrumentId: String = "",
                     val instrument: String = "",
                     val paymentOptions: PAYMENT_OPTIONS = PAYMENT_OPTIONS.PAYNOW,)

data class PaymentMethodsUiState(
    val sheetDataList: List<SheetData> = emptyList(),
    val removeCardApiState: ResourceResult<String> = ResourceResult.Initial,
    val instrumentInfoApiState: ResourceResult<String> = ResourceResult.Initial,
    val initPaymentApiState: ResourceResult<String> = ResourceResult.Initial,
)


sealed interface PaymentMethodEvent {
    data class PaymentOptionSelected(
        val instrumentId: String,
        val instrumentType: String,
        val instrument: String): PaymentMethodEvent

    data class RemoveSavedCard(
        val intrumentId: String,
        val paymentOptions: PAYMENT_OPTIONS,
        val instrument: String): PaymentMethodEvent
    data class FailedToRemoveCard(val throwable: Throwable): PaymentMethodEvent
    data class CardRemovedSuccessfully(val data: String): PaymentMethodEvent
    object GetInstrumentData: PaymentMethodEvent
    data class LogEvent(val message: String): PaymentMethodEvent
}

enum class INSTRUMENTS {
    CC, AMEX_CC, ENETS, EGIRO, BNPL, PAYNOW
}

enum class INSTRUMENT_TYPE {
    VISA, MASTERCARD, AMEX
}

enum class PAYMENT_OPTIONS {
    PAYNOW, DASH, CC, ENETS, EGIRO, BNPL, AMEX_CC, VisaMaster, VISA_CARD, MASTER_CARD
}

enum class PaymentIcon {
    VISA, MASTER, DASH, PAYNOW, AMEX, EGIRO, BNPL, ENETS, NETSCLICK, GENERIC_CC, UNKNOWN
}



// instrumentInfo
internal fun MutableStateFlow<PaymentMethodsUiState>.loadingInstrInfo() = run {
    update { it.copy(instrumentInfoApiState = ResourceResult.Loading) }
}
internal fun MutableStateFlow<PaymentMethodsUiState>.successInstrInfo(sheetDataList: List<SheetData>) = run {
    update { it.copy(sheetDataList = sheetDataList, instrumentInfoApiState = ResourceResult.Success("")) }
}
internal fun MutableStateFlow<PaymentMethodsUiState>.failedInstrInfo(throwable: Throwable?) = run {
    update { it.copy(instrumentInfoApiState = ResourceResult.Error(throwable)) }
}
// removeCard
internal fun MutableStateFlow<PaymentMethodsUiState>.loadingRemoveCardApi() = run {
    update { it.copy(removeCardApiState = ResourceResult.Loading) }
}
internal fun MutableStateFlow<PaymentMethodsUiState>.successRemoveCardApi(instrument: String, instrumentId: String) = run {
    update { uiData ->
            println("instrument $instrument instrumentId $instrumentId")
        println("sheetDataList ${uiData.copy().sheetDataList}")
        val removedList = uiData.copy().sheetDataList.filter { it.instrument != instrument && it.instrumentId != instrumentId }
        uiData.copy(sheetDataList = removedList, removeCardApiState = ResourceResult.Success(""))
    }
}
internal fun MutableStateFlow<PaymentMethodsUiState>.failedRemoveCardApi(throwable: Throwable?) = run {
    update { it.copy(removeCardApiState = ResourceResult.Error(throwable)) }
}

// initPayment
internal fun MutableStateFlow<PaymentMethodsUiState>.loadingInitPayApi() = run {
    update { it.copy(initPaymentApiState = ResourceResult.Loading) }
}
internal fun MutableStateFlow<PaymentMethodsUiState>.successInitPayApi() = run {
    update { it.copy(initPaymentApiState = ResourceResult.Success("")) }
}
internal fun MutableStateFlow<PaymentMethodsUiState>.failedInitPayApi(throwable: Throwable?) = run {
    update { it.copy(initPaymentApiState = ResourceResult.Error(throwable)) }
}


// Extention functions
fun InstrumentInfoResponse.toSheetData(): List<SheetData> {
    val sheetDataList = mutableListOf<SheetData>()
    // Add PayNow
    sheetDataList.addPayNow()
    val sortedList = instrumentInfo.sortedBy { it.ordinal }
    // Add saved cards
    sheetDataList.addSavedCards(sortedList)
    // Add non CC payment options
    sheetDataList.addNonCCInstruments(sortedList)
    // Add dash
    sheetDataList.addDash()
    return sheetDataList
}

fun MutableList<SheetData>.addNonCCInstruments(
    sortedList: List<InstrumentInfo>
){
    val nonCCList = sortedList.filterNot { it.instrument == "CC" }
    nonCCList.forEach { instrInfo ->
        add(
            SheetData(
                title = getTitle(instrInfo.instrument, "", ""),
                hyperLinkText = "",
                leftIconResource = getImageRes(instrInfo.instrument),
                serviceAvailable = true,
                instrumentId = "PAYNOW",
                instrument = instrInfo.instrument,
                paymentOptions = PAYMENT_OPTIONS.PAYNOW
            )
        )
    }
}

fun MutableList<SheetData>.addSavedCards(
    instrInfoList: List<InstrumentInfo>
){
    instrInfoList.filter { it.instrument == "CC" }.forEach { instrInfo ->
        instrInfo.cardInfo.forEach { cardInfo ->
            val leftImage = getImageRes("CC", cardInfo.instrId)
            add(
                SheetData(
                    title = getTitle("CC", cardInfo.instrId, cardInfo.instrToken),
                    hyperLinkText = "[Remove Card]({\"instrument\": \"CC\", \"instrId\": \"${cardInfo.instrId}\"})",
                    leftIconResource = leftImage,
                    serviceAvailable = true,
                    instrumentId = cardInfo.instrId,
                    instrument = instrInfo.instrument,
                    // instrToken can only be MASTERCARD or VISA, for AMEX there is separate instrument AMEX_CC
                    paymentOptions = if(cardInfo.instrToken == "MASTERCARD")
                        PAYMENT_OPTIONS.MASTER_CARD else PAYMENT_OPTIONS.VISA_CARD
                )
            )
        }
    }
}

fun MutableList<SheetData>.addDash(){
    add(
        SheetData(
        title = "Dash",
        serviceAvailable = true,
        instrumentId = "",
        instrument = "DASH",
        paymentOptions = PAYMENT_OPTIONS.DASH
        )
    )
}

fun MutableList<SheetData>.addPayNow(){
    add(
        SheetData(
            title = "PayNow",
            hyperLinkText = "",
            leftIconResource = MR.images.ic_paynow,
            serviceAvailable = true,
            instrumentId = "",
            instrument = INSTRUMENTS.PAYNOW.toString(),
            paymentOptions = PAYMENT_OPTIONS.PAYNOW
        )
    )
}

fun getImageRes(
    instrument: String,
    instrumentId: String = ""): ImageResource {
    val iconType = with(instrument) {
        when {
            equals("CC") -> {
                if(instrumentId.equals("MASTERCARD")){
                    MR.images.ic_visa
                } else if(instrumentId.equals("AMEX")){
                    MR.images.ic_amex
                } else {
                    MR.images.ic_visa
                }
            }
            equals("ENETS") -> MR.images.ic_enets
            equals("EGIRO") -> MR.images.ic_visa
            equals("AMEX_CC") -> MR.images.ic_amex
            equals("BNPL") -> MR.images.ic_visa
            else -> PaymentIcon.UNKNOWN
        }
    }
    return MR.images.ic_visa
}

fun getTitle(
    instrument: String,
    instrumentId: String,
    instrToken: String): String {
    val iconType = with(instrument) {
        when {
            equals("CC") -> {
                if(instrumentId == "MASTERCARD"){
                    "Master *${instrToken.takeLast(4)}"
                } else if(instrumentId == "AMEX"){
                    "AMEX *${instrToken.takeLast(4)}"
                } else {
                    "VISA *${instrToken.takeLast(4)}"
                }
            }
            equals("ENETS") -> "eNETS"
            equals("EGIRO") -> "eGiro"
            equals("AMEX_CC") -> "Amex"
            equals("BNPL") -> "Atome"
            else -> "Unknown"
        }
    }
    return iconType
}


