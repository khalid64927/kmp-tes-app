package com.multiplatform.app.android.ui.preview.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.multiplatform.app.MR
import com.multiplatform.app.android.ui.preview.WithTheme
import com.multiplatform.app.ui.components.core.NumberedListItem
import dev.icerock.moko.resources.compose.stringResource

@Preview
@Composable
fun NumberedItemPreview(){
    val insList = listOf(MR.strings.paynow_instruction_1, MR.strings.paynow_instruction_2)
    WithTheme {
        Column() {
            NumberedListItem(
                number = "1.",
                pointString = stringResource(MR.strings.paynow_instruction_1))
            NumberedListItem(
                number = "2.",
                pointString = stringResource(MR.strings.paynow_instruction_2))
        }
    }
}