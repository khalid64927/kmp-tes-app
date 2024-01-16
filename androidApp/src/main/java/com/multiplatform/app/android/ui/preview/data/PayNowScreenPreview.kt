package com.multiplatform.app.android.ui.preview.data


import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.multiplatform.app.android.ui.preview.WithTheme
import com.multiplatform.app.ui.screens.PayNowScreen


@Preview
@Composable
fun payNowScreenPreview(){
    WithTheme {
        PayNowScreen()
    }
}