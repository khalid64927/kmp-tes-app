package com.multiplatform.app.ui.components.otp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ResendOTPButtonWithTimer() {
    var countdown by remember { mutableStateOf(60) }
    var buttonEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Resend OTP Button with Countdown
        if (buttonEnabled) {
            ButtonWithCountdown(
                countdown = countdown,
                onClick = {
                    buttonEnabled = false
                    startTimer(60) { buttonEnabled = true }
                }
            )
        } else {
            ButtonWithCountdown(
                countdown = countdown,
                enabled = false
            )
        }
    }
}

@Composable
fun ButtonWithCountdown(
    countdown: Int,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.clickable { if (enabled) onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (countdown > 0) "Resend OTP ($countdown sec)" else "Resend OTP",
            color = if (enabled) Color.Black else Color.Gray
        )
    }
}

private fun startTimer(durationSeconds: Int, onFinish: () -> Unit) {
    GlobalScope.launch {
        for (i in durationSeconds downTo 1) {
            delay(1000)
            if (i == 1) {
                onFinish()
            }
        }
    }
}