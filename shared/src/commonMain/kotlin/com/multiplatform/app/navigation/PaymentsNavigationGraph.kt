package com.multiplatform.app.navigation

import androidx.compose.runtime.Composable
import com.multiplatform.app.ui.screens.PayNowScreen
import com.multiplatform.app.ui.screens.PaymentMethodsScreen

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
            PaymentMethodsScreen(onClick = {
                //navigator.navigate("/payNowScreen")
            })
        }
        scene("/payNowScreen") {
            PayNowScreen()
        }
    }
}