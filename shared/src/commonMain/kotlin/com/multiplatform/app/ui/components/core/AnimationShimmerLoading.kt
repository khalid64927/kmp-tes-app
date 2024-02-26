package com.multiplatform.app.ui.components.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.shimmerLoadingAnimation(): Modifier {
    return composed {

        val shimmerColors = listOf(
            Color.White.copy(alpha = 0.3f),
            Color.White.copy(alpha = 0.5f),
            Color.White.copy(alpha = 1.0f),
            Color.White.copy(alpha = 0.5f),
            Color.White.copy(alpha = 0.3f),
        )

        this.background(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(x = 100f, y = 0.0f),
                end = Offset(x = 400f, y = 270f),
            ),
        )
    }
}

@Composable
fun ComponentCircle() {
    Box(
        modifier = Modifier
            .background(color = Color.LightGray, shape = CircleShape)
            .size(100.dp)
            .shimmerLoadingAnimation() // <--- Here.
    )
}

@Composable
fun ComponentSquare() {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(24.dp))
            .background(color = Color.LightGray)
            .size(50.dp)
            .shimmerLoadingAnimation() // <--- Here.
    )
}

@Composable
fun ComponentRectangle() {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(24.dp))
            .background(color = Color.LightGray)
            .height(200.dp)
            .fillMaxWidth()
            .shimmerLoadingAnimation() // <--- Here.
    )
}

@Composable
fun ComponentRectangleLineLong() {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = Color.LightGray)
            .size(height = 25.dp, width = 200.dp)
            .shimmerLoadingAnimation() // <--- Here.
    )
}

@Composable
fun ComponentRectangleLineShort() {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color = Color.LightGray)
            .size(height = 25.dp, width = 100.dp)
            .shimmerLoadingAnimation() // <--- Here.
    )
}