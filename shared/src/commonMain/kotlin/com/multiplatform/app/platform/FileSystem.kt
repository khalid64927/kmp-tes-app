package com.multiplatform.app.platform

import androidx.compose.ui.graphics.ImageBitmap
import dev.icerock.moko.resources.FileResource

expect interface FileSystem {
    suspend fun saveBitmap(imageBitmap: ImageBitmap)
    suspend fun readText(fileResource: FileResource): String
}