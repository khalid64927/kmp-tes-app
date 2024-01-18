package com.multiplatform.app.android.ui.preview.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.multiplatform.app.android.ui.preview.WithTheme
import com.multiplatform.app.ui.designsystem.components.ClickableButton
import com.multiplatform.app.ui.designsystem.components.FilledButtonBlack


@Preview(name = "Text bold Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,)
@Composable
fun FilledButtonExamplePreview(){
    WithTheme {
        FilledButtonBlack(modifier = Modifier, onClick = { }, text = "Save QR Code")
    }

}


@Preview(name = "Text bold Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,)
@Composable
fun ClickableButtonPreview(){
    WithTheme {
        ClickableButton()
    }

}