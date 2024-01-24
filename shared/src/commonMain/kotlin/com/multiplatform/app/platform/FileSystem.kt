package com.multiplatform.app.platform

import androidx.compose.ui.graphics.ImageBitmap

expect interface FileSystem {
    suspend fun saveBitmap(imageBitmap: ImageBitmap)
}