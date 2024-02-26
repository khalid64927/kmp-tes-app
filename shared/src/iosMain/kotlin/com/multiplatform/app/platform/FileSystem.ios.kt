package com.multiplatform.app.platform

import androidx.compose.ui.graphics.ImageBitmap
import com.multiplatform.app.MR
import dev.icerock.moko.resources.FileResource

actual interface FileSystem {
    actual suspend fun saveBitmap(imageBitmap: ImageBitmap)
    actual suspend fun readText(fileResource: FileResource): String
}

class FileSystemImpl: FileSystem {

    override suspend fun saveBitmap(imageBitmap: ImageBitmap) {
        TODO("Not yet implemented")
    }

    override suspend fun readText(fileResource: FileResource): String {
        return fileResource.readText()
    }

}