package com.multiplatform.app

import androidx.compose.runtime.Composable
import com.multiplatform.app.ui.theme.AppTheme
import com.multiplatform.app.navigation.HeyaNavigationGraph
import moe.tlaster.precompose.PreComposeApp

@Composable
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean,
) = PreComposeApp {
    AppTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
    ) {
        HeyaNavigationGraph()
    }
}
