package com.multiplatform.app.ui.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.multiplatform.app.MR
import dev.icerock.moko.resources.compose.painterResource
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.QrLogoPadding
import io.github.alexzhirkevich.qrose.options.QrLogoShape
import io.github.alexzhirkevich.qrose.options.circle
import io.github.alexzhirkevich.qrose.options.solid
import io.github.alexzhirkevich.qrose.rememberQrCodePainter


@Composable
fun QrCodeComponent(modifier: Modifier, data: String){
    val logoIcon = painterResource(MR.images.heya_logo)
    val painter = rememberQrCodePainter(data) {
        logo {
            painter = logoIcon
            padding = QrLogoPadding.Natural(.1f)
            shape = QrLogoShape.circle()
            size = 0.2f
        }
        colors {
            frame = QrBrush.solid(Color.Black)
        }
    }

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
    )
}