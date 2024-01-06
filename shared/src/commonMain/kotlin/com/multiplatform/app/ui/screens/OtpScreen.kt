package com.multiplatform.app.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.app.ui.theme.prepaidYellow
import com.multiplatform.app.MR
import dev.icerock.moko.resources.compose.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.multiplatform.app.ui.components.otp.ResendOTPButtonWithTimer
import com.multiplatform.app.ui.components.otp.otpComponent
import com.multiplatform.app.ui.theme.prepaidIndigoDark
import com.multiplatform.app.viewmodels.OtpEvent
import com.multiplatform.app.viewmodels.OtpUiState
import com.multiplatform.app.viewmodels.OtpViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPScreen(
    msisdn: String,
    challengeToken: String,
    xClientCode: String,
    onNavigateToMain: () -> Unit = {},
    onNavigateToError: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier,
){
    val viewModel = getViewModel(
        key = "otp-screen",
        factory = viewModelFactory { OtpViewModel() }
    )
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit){
        viewModel.onEvent(
            OtpEvent.UpdateRequestData(
                msisdn = msisdn,
                challengeToken = challengeToken,
                xClientCode = xClientCode))
    }

    if(state.isSuccess){

    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { topBarOtpComposable(onNavigateBack = onNavigateBack, scrollBehavior = scrollBehavior) },
        content = { otpContent(msisdn = msisdn, innerPaddingValues = it, otpUiState = state, viewModel = viewModel) }
    )
}



@Composable
internal fun otpContent(
    msisdn: String,
    innerPaddingValues: PaddingValues,
    otpUiState: OtpUiState,
    viewModel: OtpViewModel
){
    LazyColumn (
        contentPadding = innerPaddingValues,
        modifier = Modifier.fillMaxSize().
        padding(top = 32.dp, start = 24.dp, end = 24.dp)){
        item {
            Spacer(modifier = Modifier.height(22.dp))
        }
        item {
            Text(
                modifier = Modifier,
                text = stringResource(MR.strings.otp_header_h1),
                style = TextStyle(
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    letterSpacing = 0.02.sp,
                )
            )
        }


        item {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(MR.strings.otp_header),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                    color = Color.Black,
                    letterSpacing = 0.02.sp,
                )
            )
        }

        item {
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = msisdn,
                style = TextStyle (
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp,
                    color = Color.Black,
                    letterSpacing = 0.02.sp,
                )
            )
        }

        item {
            otpComponent(
                otpString = otpUiState.otpNumber,
                onOtpEntered = {viewModel.onEvent(OtpEvent.UpdateOtp(it))},
                onOtpSubmit = {viewModel.onEvent(OtpEvent.OtpSubmit(it))}
            )
        }

        item {
            ResendOTPButtonWithTimer()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun topBarOtpComposable(
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigateBack: ()->Unit = {},
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = prepaidYellow,
        ),
        title = {
            Text(
                color = Color.Black,
                text = stringResource(MR.strings.otp_title),
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(
                    tint = prepaidIndigoDark,
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}








