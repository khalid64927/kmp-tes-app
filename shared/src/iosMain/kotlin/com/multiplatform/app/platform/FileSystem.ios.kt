package com.multiplatform.app.platform

import androidx.compose.ui.graphics.ImageBitmap

actual interface FileSystem {
    actual suspend fun saveBitmap(imageBitmap: ImageBitmap)
}

class FileSystemImple: FileSystem {

    override suspend fun saveBitmap(imageBitmap: ImageBitmap) {
        TODO("Not yet implemented")
    }

}