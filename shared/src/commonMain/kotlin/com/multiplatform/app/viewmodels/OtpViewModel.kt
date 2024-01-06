package com.multiplatform.app.viewmodels

import com.multiplatform.app.data.remote.requests.SubmitOtpData
import com.multiplatform.app.domain.interactor.generateOtp.GenerateOtpInteractor
import com.multiplatform.app.domain.interactor.submitOtp.SubmitOtpInteractor
import com.multiplatform.app.common.BaseViewModel
import com.multiplatform.app.data.local.datastore.PreferencesRepository
import com.multiplatform.app.domain.models.ResourceResult
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import org.koin.core.component.get
import org.koin.core.qualifier.named

class OtpViewModel : BaseViewModel(){

    private val _state = MutableStateFlow(OtpUiState())
    val state: StateFlow<OtpUiState> = _state.asStateFlow()
    private val httpClient : HttpClient = get(named("prepaidClient"))
    private val prepaidRepository: PreferencesRepository = get()
    private val generateOtpInteractor: GenerateOtpInteractor = get()
    private val submitOtpInteractor: SubmitOtpInteractor = get()

    var updateRequestData: OtpEvent.UpdateRequestData? = null

    fun submitOtp(){
        println("submitOtp $updateRequestData")
        launchAsync {
            updateRequestData?.run {
                submitOtpInteractor.invoke(SubmitOtpData(
                    challengeToken = challengeToken,
                    code = _state.value.otpNumber,
                    xClientCode = xClientCode
                )).collectLatest {
                    when(it){
                        is ResourceResult.Success -> { _state.success() }
                        is ResourceResult.Error -> { _state.error() }
                        is ResourceResult.Loading -> { _state.loading() }
                    }
                }
            }

        }
    }

    fun onEvent(otpEvent: OtpEvent){
        when(otpEvent){
            is OtpEvent.UpdateOtp -> {
                println("OtpEvent $otpEvent")
                _state.updateOtp(otpEvent.otpNumber)
            }
            is OtpEvent.Loading -> { _state.loading() }
            is OtpEvent.Error -> { _state.error() }
            is OtpEvent.Success -> { _state.success() }
            is OtpEvent.OtpSubmit -> { submitOtp()}
            is OtpEvent.UpdateRequestData -> { updateRequestData = otpEvent }
        }

    }

}


sealed interface OtpEvent {
    data class UpdateOtp(val otpNumber: String) : OtpEvent
    object Loading : OtpEvent
    data class Error(val throwable: Throwable): OtpEvent
    data class Success(val data: String) : OtpEvent
    data class OtpSubmit(val data: String) : OtpEvent
    data class UpdateRequestData(val msisdn: String, val challengeToken: String, val xClientCode: String): OtpEvent
}


data class OtpUiState(
    val otpNumber: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val initialState: Boolean = true,
    val errorMessage: String = "",
    val isSuccess: Boolean = false,
)

internal fun MutableStateFlow<OtpUiState>.loading() = run {
    update { it.copy(isError = false, isLoading = true, isSuccess = false) }
}

internal fun MutableStateFlow<OtpUiState>.error() = run {
    update { it.copy(isError = true, isLoading = false, isSuccess = false) }
}

internal fun MutableStateFlow<OtpUiState>.success() = run {
    update { it.copy(isError = false, isLoading = false, isSuccess = true) }
}

internal fun MutableStateFlow<OtpUiState>.updateOtp(otpString: String) = run {
    update { it.copy(otpNumber = otpString) }
}