package com.multiplatform.app.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.app.ui.components.login.LoginBackgroundComponent
import com.multiplatform.app.ui.components.login.NumberInputField
import com.multiplatform.app.ui.components.login.loginButtonComponent
import com.multiplatform.app.ui.theme.prepaidErrorColor
import com.multiplatform.app.viewmodels.LoginEvent
import com.multiplatform.app.viewmodels.LoginViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory


@Composable
fun LoginScreen(
    onNavigateToOtp: (String, String, String) -> Unit,
    modifier: Modifier = Modifier,
){
    val viewModel = getViewModel(
        key = "login-screen",
        factory = viewModelFactory { LoginViewModel() }
    )
    val state by viewModel.state.collectAsState()
    if(state.navigateToOTP){
        println(" state $state")
        onNavigateToOtp(state.msisdn, state.challengeToken, state.xClientCode)
    }
    LoginBackgroundComponent {
        NumberInputField(
            query = state.msisdn,
            onValueChange = { viewModel.onEvent(LoginEvent.UpdateNumber(it))},
            maxCharLimit = 8,
            validationError = state.validationError)

        if(state.isError){
            println("error message ${state.errorMessage}")
            Row {
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    color = prepaidErrorColor,
                    style = TextStyle(
                        fontSize = 16.sp,
                        lineHeight = 28.sp,
                    ),
                    text = state.errorMessage
                )
            }
        }
        loginButtonComponent(
            modifier = Modifier,
            loginButtonUiState = state.loginButtonUiState,
            onClick = {viewModel.onEvent(LoginEvent.GetSMSOtp)}
        )

    }
}