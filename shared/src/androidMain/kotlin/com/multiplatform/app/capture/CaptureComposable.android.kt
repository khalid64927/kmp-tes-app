package com.multiplatform.app.capture

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun CaptureComposable(
    modifier: Modifier,
    controller: CaptureController,
    content: @Composable () -> Unit
) {
    println("CaptureComposable.android.kt")
    Column(modifier = modifier.capturable(controller)) {
        content()
    }

}