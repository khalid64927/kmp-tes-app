package com.multiplatform.app.navigation

import androidx.compose.runtime.Composable
import com.multiplatform.app.ui.screens.DialogExamples

import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator

@Composable
fun PaymentsNavigationGraph(navigator: Navigator = rememberNavigator()){
    NavHost(
        navigator = navigator,
        initialRoute = "/dialogExamples",
    ) {
        scene("/dialogExamples") {
            DialogExamples()
        }
    }
}