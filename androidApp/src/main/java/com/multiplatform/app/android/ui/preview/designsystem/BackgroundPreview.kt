package com.multiplatform.app.android.ui.preview.designsystem

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.multiplatform.app.ui.designsystem.components.prepaidBackground


@Preview
@Composable
fun BackgroundPreview() {
    prepaidBackground(modifier = Modifier.size(50.dp)){}

}