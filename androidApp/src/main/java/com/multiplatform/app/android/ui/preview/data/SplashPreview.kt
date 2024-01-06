package com.multiplatform.app.android.ui.preview.data

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.multiplatform.app.ui.screens.SplashScreen


@Preview
@Composable
fun SplashComposablePreview(){
    SplashScreen(onNavigateNext = { /*no-op*/ })
}

