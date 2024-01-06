package com.multiplatform.app

import androidx.compose.runtime.Composable
import com.multiplatform.app.ui.theme.AppTheme
import com.multiplatform.app.navigation.PaymentsNavigationGraph

@Composable
fun PaymentsMFE(
    darkTheme: Boolean,
    dynamicColor: Boolean,
) {
    AppTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
    ) {
        PaymentsNavigationGraph()
    }
}
