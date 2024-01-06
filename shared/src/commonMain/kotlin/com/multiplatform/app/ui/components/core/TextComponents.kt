package com.multiplatform.app.ui.components.core

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun text16sp(
    stringResource: StringResource
){
    Text(
        style = TextStyle(
            fontSize = 16.sp,
            lineHeight = 28.sp,
        ),
        text = stringResource(stringResource)
    )
}