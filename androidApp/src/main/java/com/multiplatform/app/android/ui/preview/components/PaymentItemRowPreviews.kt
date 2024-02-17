package com.multiplatform.app.android.ui.preview.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.multiplatform.app.MR
import com.multiplatform.app.android.ui.preview.WithTheme
import com.multiplatform.app.ui.designsystem.components.PaymentItemRow
import com.multiplatform.app.ui.designsystem.components.SheetConfig

@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES,)
@Composable
fun PaymentItemRowPreview(){
    val sheetConfig = SheetConfig(
        leftImageResource = MR.images.heya_logo,
        rightImageResource = MR.images.heya_logo,
        title = MR.strings.paynow_title,
        subTitle = MR.strings.paynow_title,
        hyperLinkText = MR.strings.sheet_cc_remove)
    WithTheme {
        PaymentItemRow(sheetConfig)
    }
}

