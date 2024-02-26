package com.multiplatform.app.viewmodels

import com.multiplatform.app.common.BaseViewModel
import com.multiplatform.app.data.remote.config.AppData
import com.multiplatform.app.domain.interactor.payNow.GetPayNowInteractor
import com.multiplatform.app.domain.models.ResourceResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import org.koin.core.component.get

class PayNowViewModel: BaseViewModel() {
    private val appData: AppData = get()
    private val payNowInteractor: GetPayNowInteractor = get()
    private val _state = MutableStateFlow(PayNowUiState(countDownInMilliSec = appData.timeOutInMillis))
    val state: StateFlow<PayNowUiState> = _state.asStateFlow()

    // handle user interactions
    fun onEvent(event: PayNowUserEvent){
        when(event){
            is PayNowUserEvent.GetPayNowData -> getOrRefreshPayNow()
            is PayNowUserEvent.CheckPayNowStatus -> checkPayNowStatus()
            is PayNowUserEvent.SaveQRCode -> saveQrCode()
            is PayNowUserEvent.NavigateBack -> navigateBack()
        }
    }

    private fun navigateBack(){
        // TODO
    }

    private fun getOrRefreshPayNow(){
        launchAsync {
            payNowInteractor.invoke().collectLatest {
                when(it) {
                    ResourceResult.Loading -> _state.loading()
                    is ResourceResult.Error -> _state.payNowQrFailed()
                    is ResourceResult.Success -> _state.payNowQrSuccess()
                    else -> {}
                }
            }
        }

    }

    private fun checkPayNowStatus(){
        // TODO
    }

    private fun saveQrCode(){
        // TODO
    }


}


interface PayNowUserEvent {
    data class GetPayNowData(val paymentToken: String, val correlationId: String): PayNowUserEvent
    data class CheckPayNowStatus(val paymentToken: String, val correlationId: String): PayNowUserEvent
    data class SaveQRCode(val message: String): PayNowUserEvent
    data class NavigateBack(val message: String): PayNowUserEvent
}

internal fun MutableStateFlow<PayNowUiState>.loading() = run {
    update { it.copy(getQRFailedLoadingStatus = true) }
}

internal fun MutableStateFlow<PayNowUiState>.payNowQrSuccess() = run {
    update { it.copy(getQRSuccess = true) }
}
internal fun MutableStateFlow<PayNowUiState>.payNowQrFailed() = run {
    update { it.copy(getQRFailed = true) }
}

internal fun MutableStateFlow<PayNowUiState>.timeout() = run {
    update { it.copy(getQRFailed = true) }
}

data class PayNowUiState(
    val qrCodeData: String = "",
    val countDownInMilliSec: Long = 10 * 60,
    val hasTimeoutHappened: Boolean = false,
    val amount: String = "",
    val referenceNumber: String = "",
    val getQRFailedLoadingStatus: Boolean = false,
    val getQRSuccess: Boolean = false,
    val getQRFailed: Boolean = false,
    val paymentNotReceivedStatus: Boolean = false,
    val paymentReceivedStatus: Boolean = false,
    val paymentNoReceivedYetStatus: Boolean = false,
)
