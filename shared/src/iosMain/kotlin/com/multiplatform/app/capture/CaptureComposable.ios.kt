package com.multiplatform.app.capture

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun CaptureComposable(
    modifier: Modifier,
    controller: CaptureController,
    content: @Composable () -> Unit
) {
    println("CaptureComposable.ios.kt")
    controller.content = content
    Column(modifier = modifier.capturable(controller).onGloballyPositioned { coordinates ->

    }) {
        content()
    }
}


