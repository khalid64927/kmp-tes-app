package com.multiplatform.app.ui.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun FilledButtonBlack(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit) {
    Button(onClick = { onClick() },
        modifier = modifier
    ) {
        TitleBoldWhite(string = text)
    }
}

@Composable
fun ClickableButton() {
    var clicked by remember { mutableStateOf(false) }

    Button(
        onClick = { clicked = !clicked },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(50.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        Text(text = if (clicked) "Clicked!" else "Click me")
    }
}