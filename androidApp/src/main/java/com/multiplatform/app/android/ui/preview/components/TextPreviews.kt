package com.multiplatform.app.android.ui.preview.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.multiplatform.app.MR
import com.multiplatform.app.android.ui.preview.WithTheme
import com.multiplatform.app.ui.designsystem.components.Body2Text
import com.multiplatform.app.ui.designsystem.components.Body2BoldText
import com.multiplatform.app.ui.designsystem.components.HyperlinkText
import com.multiplatform.app.ui.designsystem.components.TitleBoldWhite
import com.multiplatform.app.ui.designsystem.components.TitleTextBold
import com.multiplatform.app.ui.designsystem.components.TitleTextRegular
import dev.icerock.moko.resources.compose.stringResource


@Preview(name = "Text bold Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,)
@Composable
fun TitleTextBoldPreview(){
    WithTheme {
        TitleTextBold(string = stringResource(MR.strings.paynow_title))
    }
}

@Preview(name = "Text regular Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,)
@Composable
fun TitleTextRegularPreview(){
    WithTheme {
        TitleTextRegular(string = stringResource(MR.strings.paynow_title))
    }
}

@Preview(name = "Text regular Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,)
@Composable
fun HyperLinkTextPreview(){
    WithTheme {
        HyperlinkText(
            string = stringResource(MR.strings.sheet_cc_remove ),
            onClick = {_, _ -> })
    }
}

@Preview(name = "Text regular Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,)
@Composable
fun Body2TextPreview(){
    WithTheme {
        Body2Text(string = stringResource(MR.strings.paynow_ref, "92839238"))
    }
}

@Preview(name = "Text regular Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO,)
@Composable
fun Body2TextBoldPreview(){
    WithTheme {
        Body2BoldText(string = stringResource(MR.strings.paynow_ref, "92839238"))
    }
}

@Preview(name = "Text regular Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES,)
@Composable
fun TitleBoldWhitePreview(){
    TitleBoldWhite(string = stringResource(MR.strings.paynow_ref, "92839238"))
}

