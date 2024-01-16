package com.multiplatform.app.android.ui.preview.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.multiplatform.app.MR
import com.multiplatform.app.android.ui.preview.WithTheme
import com.multiplatform.app.ui.designsystem.components.TopNavigation
import com.multiplatform.app.ui.designsystem.components.TopNavigationConfig

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,)
@Composable
fun TopNavigationPreview1(){
    WithTheme {
        TopNavigation(
            TopNavigationConfig(
                stringResource = MR.strings.paynow_title,
                leftImageResource = MR.images.heya_logo,
                rightImageResource = MR.images.heya_logo)
        )
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,)
@Composable
fun TopNavigationPreview2(){
    WithTheme {
        TopNavigation(
            TopNavigationConfig(
                stringResource = MR.strings.paynow_title,
                leftImageResource = MR.images.heya_logo)
        )
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,)
@Composable
fun TopNavigationPreview3(){
    WithTheme {
        TopNavigation(
            TopNavigationConfig(
                stringResource = MR.strings.paynow_title)
        )
    }
}