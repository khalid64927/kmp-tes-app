package com.multiplatform.app.ui.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.app.util.isNull
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun PaymentItemRow(sheetConfig: SheetConfig){
    Box(
        modifier = Modifier.fillMaxWidth().padding(5.dp).
        padding(PaddingValues(start = 10.dp, end = 10.dp)),
        contentAlignment = Alignment.CenterStart) {
        Image(
            modifier = Modifier.size(width = 30.dp, height = 30.dp).
            padding(PaddingValues(start = 0.dp)),
            painter = painterResource(sheetConfig.leftImageResource),
            contentDescription = "Left Icons")

        Column(modifier = Modifier.padding(PaddingValues(start = 40.dp))) {
            Text(
                modifier = Modifier,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                ),
                text = stringResource(sheetConfig.title)
            )

            if(sheetConfig.hyperLinkText.isNull()){
                sheetConfig.subTitle?.run {
                    Text(
                        modifier = Modifier,
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 28.sp,
                        ),
                        text = stringResource(this)
                    )
                }
            } else {
                sheetConfig.hyperLinkText?.run {
                    HyperlinkText(stringResource(this), onClick = sheetConfig.onClick)
                }
            }
        }

        sheetConfig.rightImageResource?.run {
            Image(
                modifier = Modifier.size(width = 30.dp, height = 30.dp).
                padding(PaddingValues(end = 0.dp)).align(Alignment.CenterEnd),
                painter = painterResource(this),
                contentDescription = "Right Icons")
        }
    }
}

data class SheetConfig(
    val title: StringResource,
    val subTitle: StringResource?,
    val hyperLinkText: StringResource?,
    val leftImageResource: ImageResource,
    var rightImageResource: ImageResource?,
    val onClick : (token: String, linkType: LinkType)-> Unit = {_, _ ->},
)

