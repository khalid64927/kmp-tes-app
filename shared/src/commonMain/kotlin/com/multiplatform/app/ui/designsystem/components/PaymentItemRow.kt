package com.multiplatform.app.ui.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import com.multiplatform.app.MR
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun PaymentItemRow(){
    Row(modifier = Modifier
        .fillMaxWidth().background(color = Color.White)
        .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Image(
            alignment = Alignment.CenterStart,
            modifier = Modifier.size(width = 30.dp, height = 30.dp).padding(end = 10.dp),
            painter = painterResource(MR.images.heya_logo),
            contentDescription = "Splash dots Graphics")

        Column(modifier = Modifier.padding(2.dp)) {
            Text(
                modifier = Modifier,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                ),
                text = "Master"
            )
            Text(
                modifier = Modifier,
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 28.sp,
                ),
                text = "************1234"
            )

        }

        Image(
            alignment = Alignment.CenterEnd,
            modifier = Modifier.size(width = 30.dp, height = 30.dp),
            painter = painterResource(MR.images.heya_logo),
            contentDescription = "Splash dots Graphics")

    }

}