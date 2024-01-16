package com.multiplatform.app.android.ui.preview.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.multiplatform.app.MR
import com.multiplatform.app.android.ui.preview.WithTheme
import com.multiplatform.app.ui.designsystem.components.Body2Text
import com.multiplatform.app.ui.designsystem.components.HyperlinkText
import com.multiplatform.app.ui.designsystem.components.TitleTextBold
import com.multiplatform.app.ui.designsystem.components.TitleTextRegular


@Preview(name = "Text bold Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,)
@Composable
fun TitleTextBoldPreview(){
    WithTheme {
        TitleTextBold(stringResource = MR.strings.paynow_title)
    }
}

@Preview(name = "Text regular Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,)
@Composable
fun TitleTextRegularPreview(){
    WithTheme {
        TitleTextRegular(stringResource = MR.strings.paynow_title)
    }
}

@Preview(name = "Text regular Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,)
@Composable
fun HyperLinkTextPreview(){
    WithTheme {
        HyperlinkText(
            stringResource = MR.strings.sheet_cc_remove,
            onClick = {_, _ -> })
    }
}

@Preview(name = "Text regular Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,)
@Composable
fun Body2TextPreview(){
    WithTheme {
        Body2Text(stringResource = MR.strings.sheet_cc_remove)
    }
}

