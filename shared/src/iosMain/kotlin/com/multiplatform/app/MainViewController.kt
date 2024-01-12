package com.multiplatform.app

import androidx.compose.ui.window.ComposeUIViewController
import com.multiplatform.app.ui.custom.TransparentComposeUIViewController
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceStyle

fun MainViewController() = ComposeUIViewController {
    val isDarkTheme =
        UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    App(
        darkTheme = isDarkTheme,
        dynamicColor = false,
    )
}

fun TransparentMainViewController() = TransparentComposeUIViewController {
    val isDarkTheme =
        UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
    App(
        darkTheme = isDarkTheme,
        dynamicColor = false,
    )
}


