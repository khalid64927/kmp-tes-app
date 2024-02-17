package com.multiplatform.app.platform

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import android.content.ContentValues
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.compose.ui.graphics.asAndroidBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual interface FileSystem {
    actual suspend fun saveBitmap(imageBitmap: ImageBitmap)
}

class FileSystemImple: FileSystem, KoinComponent {

    private val context: Context = get()

    @RequiresPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    override suspend fun saveBitmap(imageBitmap: ImageBitmap) {
         withContext(Dispatchers.IO) {
             // Convert ImageBitmap to Bitmap
             val bitmap: Bitmap = imageBitmap.asAndroidBitmap()
            // Insert image into MediaStore
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.TITLE, "payNowQR")
                put(MediaStore.Images.Media.DESCRIPTION, "Singtel_PayNow_QR")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg") // Adjust mime type if needed
            }

            // Save the image
            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            try {
                // Open an output stream to write the bitmap data to the content URI
                context.contentResolver.openOutputStream(uri!!)?.use { outputStream ->
                    // Compress the bitmap to the output stream
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }

                // Display a success message
                showToast(context, "Image saved to gallery")

            } catch (e: Exception) {
                e.printStackTrace()
                // Display an error message
                showToast(context, "Failed to save image")
            }
        }
    }

    private suspend fun showToast(context: Context, message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

}