package com.multiplatform.app.android

import android.os.Bundle
import com.multiplatform.app.App
import moe.tlaster.precompose.lifecycle.PreComposeActivity
import moe.tlaster.precompose.lifecycle.setContent

class MainActivity : PreComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(
                //darkTheme = isSystemInDarkTheme(),
                darkTheme = false,
                dynamicColor = true,
            )
        }
    }
}
