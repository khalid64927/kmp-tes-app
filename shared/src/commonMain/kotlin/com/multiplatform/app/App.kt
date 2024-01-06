package com.multiplatform.app

import androidx.compose.runtime.Composable
import com.multiplatform.app.ui.theme.AppTheme
import com.multiplatform.app.navigation.HeyaNavigationGraph

@Composable
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean,
) {
    AppTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
    ) {
        HeyaNavigationGraph()
    }
}
