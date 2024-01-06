package com.multiplatform.app.android.ui.preview.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.multiplatform.app.ui.screens.LoginScreen


@Preview
@Composable
fun LoginPreview(){
    LoginScreen(onNavigateToOtp = { msisdn, challengeToken, xClientCode ->  })
}
