package com.multiplatform.app.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.multiplatform.app.ui.screens.AccountScreen
import com.multiplatform.app.ui.screens.CommonErrorScreen
import com.multiplatform.app.ui.screens.CommonWebViewScreen
import com.multiplatform.app.ui.screens.DialogExamples
import com.multiplatform.app.ui.screens.HomeScreen
import com.multiplatform.app.ui.screens.IDDInfoScreen
import com.multiplatform.app.ui.screens.LoginScreen
import com.multiplatform.app.ui.screens.OTPScreen
import com.multiplatform.app.ui.screens.PlanDetailsScreen
import com.multiplatform.app.ui.screens.ReviewOrderScreen
import com.multiplatform.app.ui.screens.RoamingTipsScreen
import com.multiplatform.app.ui.screens.SplashScreen
import com.multiplatform.app.ui.screens.TermsAndConditionScreen
import com.multiplatform.app.ui.screens.TopUpListScreen
import com.multiplatform.app.ui.screens.UsageDetailsScreen
import com.multiplatform.app.ui.screens.WalletScreen
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator


@Composable
fun HeyaNavigationGraph(navigator: Navigator = rememberNavigator()){
    NavHost(
        navigator = navigator,
        initialRoute = "/dialogExamples",
    ) {
        scene("/splash") {
            SplashScreen(
                onNavigateNext = { navigator.navigate("/termsAndConditions") },
            )
        }
        scene("/termsAndConditions") {
            TermsAndConditionScreen(
                onNavigateToSearch = { navigator.navigate("/login") },
            )
        }
        scene("/login") {
            LoginScreen(
                onNavigateToOtp = {
                        msisdn, challengeToken,xClientCode  ->
                    navigator.navigate(
                        "/otp/$msisdn/$challengeToken/$xClientCode"
                    ) },
                modifier = Modifier.fillMaxSize())
        }
        scene("/otp/{msisdn}/{challengeToken}/{xClientCode}") { backStackEntry ->
            val msisdn: String? = backStackEntry.path("msisdn")
            val challengeToken: String? = backStackEntry.path("challengeToken")
            val xClientCode: String? = backStackEntry.path("xClientCode")
            println("msisdn is $msisdn")
            println("challengeToken is $challengeToken")
            println("xClientCode is $xClientCode")
            OTPScreen(
                msisdn = msisdn.orEmpty(),
                challengeToken = challengeToken.orEmpty(),
                xClientCode = xClientCode.orEmpty(),
                onNavigateBack = {navigator.popBackStack()},
                onNavigateToMain = { navigator.navigate("/home") },
                onNavigateToError = { navigator.navigate("/error") },
                modifier = Modifier.fillMaxSize())
        }
        // TODO: not yet connected
        scene("/account") {
            AccountScreen(
                onNavigateToSearch = { navigator.navigate("/account") },
                modifier = Modifier.fillMaxSize())
        }
        scene("/error") {
            CommonErrorScreen(
                onNavigateToSearch = { navigator.navigate("/account") },
                modifier = Modifier.fillMaxSize())
        }
        scene("/webView") {
            CommonWebViewScreen(
                onNavigateToSearch = { navigator.navigate("/account") },
                modifier = Modifier.fillMaxSize())
        }
        scene("/home") {
            HomeScreen(
                onNavigateToSearch = { navigator.navigate("/home") },
                modifier = Modifier.fillMaxSize())
        }
        scene("/iddInfo") {
            IDDInfoScreen(
                onNavigateToSearch = { navigator.navigate("/home") },
                modifier = Modifier.fillMaxSize())
        }

        scene("/planDetail") {
            PlanDetailsScreen(
                onNavigateToSearch = { navigator.navigate("/account") },
                modifier = Modifier.fillMaxSize())
        }
        scene("/reviewDetails") {
            ReviewOrderScreen(
                onNavigateToSearch = { navigator.navigate("/account") },
                modifier = Modifier.fillMaxSize())
        }
        scene("/roamingTips") {
            RoamingTipsScreen(
                onNavigateToSearch = { navigator.navigate("/account") },
                modifier = Modifier.fillMaxSize())
        }

        scene("/topup") {
            TopUpListScreen(
                onNavigateToSearch = { navigator.navigate("/home") },
                modifier = Modifier.fillMaxSize())
        }
        scene("/usageDetails") {
            UsageDetailsScreen(
                onNavigateToSearch = { navigator.navigate("/account") },
                modifier = Modifier.fillMaxSize())
        }
        scene("/walletScreen") {
            WalletScreen(
                onNavigateToSearch = { navigator.navigate("/walletScreen") },
                modifier = Modifier.fillMaxSize())
        }

        scene("/dialogExamples") {
            DialogExamples()
        }
    }
}
