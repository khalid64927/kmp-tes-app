package com.multiplatform.app.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.multiplatform.app.App

class MainActivity : ComponentActivity() {
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
