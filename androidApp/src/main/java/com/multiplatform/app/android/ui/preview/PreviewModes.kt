package com.multiplatform.app.android.ui.preview

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.multiplatform.app.ui.theme.AppTheme


@Preview(name = "Dark Mode", showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES,)
@Preview(name = "Light Mode", showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_NO)
annotation class ThemePreviews



@Composable
fun WithTheme(content: @Composable () -> Unit){
    AppTheme(darkTheme = true, dynamicColor = true) {
        Surface {
            content()
        }
    }
}