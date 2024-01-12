package com.multiplatform.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import com.multiplatform.app.PaymentsMFE


class PaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContent {
            PaymentsMFE(darkTheme = isSystemInDarkTheme(), dynamicColor = true)
        }
    }
}
