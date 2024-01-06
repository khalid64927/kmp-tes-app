package com.multiplatform.app.viewmodels

import com.multiplatform.app.data.remote.config.PrepaidApiException
import com.multiplatform.app.domain.interactor.apigee.ApiGeeInteractor
import com.multiplatform.app.domain.interactor.generateOtp.GenerateOtpInteractor
import com.multiplatform.app.domain.interactor.registerDevice.RegisterDeviceInteractor
import com.multiplatform.app.common.BaseViewModel
import com.multiplatform.app.data.local.datastore.PreferencesRepository
import com.multiplatform.app.domain.models.ResourceResult
import io.ktor.client.HttpClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import org.koin.core.component.get
import org.koin.core.qualifier.named

class LoginViewModel: BaseViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()
    private val httpClient : HttpClient = get(named("prepaidClient"))
    private val prepaidRepository: PreferencesRepository = get()
    private val apiGeeInteractor: ApiGeeInteractor = get()
    private val registerDeviceInteractor: RegisterDeviceInteractor = get()
    private val generateOtpInteractor: GenerateOtpInteractor = get()


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loginApi(){
        launchAsync {
            var xClientCode: String = ""
            val combinedFlow = registerDeviceInteractor.invoke().flatMapConcat { registrationResult ->
                if (registrationResult is ResourceResult.Success) {
                    // If registration is successful, proceed with the login
                    xClientCode = registrationResult.data.id
                    generateOtpInteractor.invoke(
                        msisdn = _state.value.msisdn,
                        xClientCode = registrationResult.data.id
                    ) // Replace with your actual login interactor call
                } else if (registrationResult is ResourceResult.Loading) {
                    flowOf(ResourceResult.Loading)
                } else {
                    flowOf(registrationResult as ResourceResult.Error)
                }
            }
            combinedFlow.collectLatest { result ->
                when (result) {
                    is ResourceResult.Loading -> onEvent(LoginEvent.Loading)
                    is ResourceResult.Success -> onEvent(LoginEvent.NavigateToOTP(result.data.challengeToken, xClientCode))
                    is ResourceResult.Error -> {
                        if(result.throwable is PrepaidApiException){
                            println("error ${result.throwable.responseBody}")
                        }
                        onEvent(LoginEvent.Error(result.throwable ?: Exception("Something went wrong!")))
                    }
                }
            }
        }
    }

    fun onEvent(event: LoginEvent){
        when(event){
            is LoginEvent.Error -> { _state.error(event.throwable.message.orEmpty()) }
            is LoginEvent.Loading -> {_state.loading()}
            is LoginEvent.UpdateNumber -> { _state.update { it.copy(msisdn = event.msisdn) } }
            is LoginEvent.GetSMSOtp -> {
                if(state.value.msisdn.length != 8){
                    _state.error("Enter a valid 8-digit heya number. For number transfers, wait till it has completed before logging in again. ", validationError = true)
                    return
                }
                loginApi()
            }
            is LoginEvent.Success -> { _state.success()}
            is LoginEvent.NavigateToOTP -> { _state.navigate(event.challengeToken, event.xClientCode) }
        }
    }
}

sealed interface LoginEvent {
    data class UpdateNumber(val msisdn: String) : LoginEvent
    object Loading : LoginEvent
    data class Error(val throwable: Throwable): LoginEvent
    data class Success(val data: String) : LoginEvent
    object GetSMSOtp: LoginEvent
    data class NavigateToOTP(val challengeToken: String, val xClientCode: String): LoginEvent
}


data class LoginUiState(
    val validationError: Boolean = false,
    val msisdn: String = "",
    val challengeToken: String = "",
    val xClientCode: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val initialState: Boolean = true,
    val errorMessage: String = "",
    val isSuccess: Boolean = false,
    val navigateToOTP: Boolean = false,
    val loginButtonUiState: LoginButtonUiState = LoginButtonUiState()
)

internal fun MutableStateFlow<LoginUiState>.loading() = run {
    update { it.copy(isLoading = true, loginButtonUiState = LoginButtonUiState.loading)}
}
internal fun MutableStateFlow<LoginUiState>.error(errorMessage: String, validationError: Boolean = false) = run {
    update { it.copy(
        errorMessage = errorMessage,
        isError = true,
        loginButtonUiState = LoginButtonUiState.error,
        validationError = validationError)}
}

internal fun MutableStateFlow<LoginUiState>.navigate(challengeToken: String, xClientCode: String) = run {
    update { it.copy(navigateToOTP = true, challengeToken = challengeToken, xClientCode = xClientCode)}
}

internal fun MutableStateFlow<LoginUiState>.success() = run {
    update {
        it.copy(isSuccess = true, loginButtonUiState = LoginButtonUiState.success)
    }
}

data class LoginButtonUiState(
    val initialState: Boolean = true,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false) {
    companion object {
        val loading = LoginButtonUiState(initialState = false, isLoading = true)
        val success = LoginButtonUiState(initialState = false, isSuccess = true)
        val error = LoginButtonUiState(initialState = false, isError = true)

    }

    fun allowClick() = !(isSuccess || isLoading)

}
