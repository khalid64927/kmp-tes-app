package com.multiplatform.app.android.ui.preview.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.multiplatform.app.MR
import com.multiplatform.app.android.ui.preview.WithTheme
import com.multiplatform.app.ui.components.bottomsheet.CenteredLoadingComponent
import com.multiplatform.app.ui.designsystem.components.PaymentItemRow
import com.multiplatform.app.viewmodels.SheetData

@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES,)
@Composable
fun PaymentItemRowPreview(){
    val sheetConfig = SheetData(
        leftIconResource = MR.images.ic_visa,
        rightImageResource = MR.images.ic_right_arrow,
        title = "Visa *3215",
        hyperLinkText = "[Remove](www.singtel.com)")
    WithTheme {
        PaymentItemRow(sheetConfig)
        MR.strings
    }
}



@Preview
@Composable
fun circularProgress(){
    Column(
        modifier = Modifier.height(60.dp)
            .padding(0.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenteredLoadingComponent()
    }


}

@Preview
@Composable
fun PaymentMethodsScreen(){
    CenteredLoadingComponent()

}

