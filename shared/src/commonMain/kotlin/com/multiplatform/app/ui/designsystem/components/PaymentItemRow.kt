package com.multiplatform.app.ui.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.app.viewmodels.SheetData
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun PaymentItemRow(
    sheetData: SheetData,
    onClick: (token: String, linkType: LinkType)->Unit = {_, _ -> }
){
    Box(
        modifier = Modifier.fillMaxWidth().padding(5.dp).background(Color.Gray).
        padding(PaddingValues(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)),
        contentAlignment = Alignment.CenterStart) {
        Image(
            modifier = Modifier.size(width = 30.dp, height = 30.dp).
            padding(PaddingValues(start = 0.dp)),
            painter = painterResource(sheetData.leftIconResource),
            contentDescription = "Left Icons")

        Column(modifier = Modifier.padding(PaddingValues(start = 40.dp))) {
            Text(
                modifier = Modifier,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                ),
                text = sheetData.title
            )

            sheetData.hyperLinkText?.run {
                HyperlinkText(this, onClick = onClick)
            }
        }
        sheetData.rightImageResource?.run {
            Image(
                modifier = Modifier.size(width = 16.dp, height = 16.dp).
                padding(PaddingValues(end = 0.dp)).align(Alignment.CenterEnd),
                painter = painterResource(this),
                contentDescription = "Right Icons")
        }
    }
}

