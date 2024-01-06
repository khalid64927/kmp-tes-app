package com.multiplatform.app.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.compose.foundation.isSystemInDarkTheme
import com.multiplatform.app.PaymentsMFE
import moe.tlaster.precompose.lifecycle.PreComposeActivity
import moe.tlaster.precompose.lifecycle.setContent


class PaymentActivity : PreComposeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContent {
            PaymentsMFE(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = true,
            )
        }
    }
}
