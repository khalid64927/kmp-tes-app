package com.multiplatform.app.capture

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
expect fun CaptureComposable(
    modifier: Modifier,
    controller: CaptureController,
    content: @Composable ()-> Unit)