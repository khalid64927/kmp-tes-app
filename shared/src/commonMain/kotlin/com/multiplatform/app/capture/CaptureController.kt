package com.multiplatform.app.capture

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.LayoutCoordinates
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Controller for capturing [Composable] content.
 */
class CaptureController internal constructor() {

    /**
     * Medium for providing capture requests
     */
    private val _captureRequests = MutableSharedFlow<CaptureRequest>(extraBufferCapacity = 1)
    internal val captureRequests = _captureRequests.asSharedFlow()
    /* This value will be set in iOSMain */
    var content : @Composable () -> Unit = {}

    data class iOSScreenshotState(
        var content : @Composable () -> Unit = {},
        var coordinates: LayoutCoordinates? = null)


    /**
     * Creates and requests for a Bitmap capture with specified [config] and returns
     * an [ImageBitmap] asynchronously.
     *
     * This method is safe to be called from the "main" thread directly.
     *
     * Make sure to call this method as a part of callback function and not as a part of the
     * [Composable] function itself.
     *
     * @param config Bitmap config of the desired bitmap. Defaults to [Bitmap.Config.ARGB_8888]
     */
    @ExperimentalComposeApi
    fun captureAsync(): Deferred<ImageBitmap> {
        val deferredImageBitmap = CompletableDeferred<ImageBitmap>()
        return deferredImageBitmap.also {
            _captureRequests.tryEmit(CaptureRequest(imageBitmapDeferred = it, content = content))
        }
    }

    /**
     * Holds information of capture request
     */
    internal class CaptureRequest(
        val imageBitmapDeferred: CompletableDeferred<ImageBitmap>,
        // for non android targets
        var content : @Composable () -> Unit

    )
}

/**
 * Creates [CaptureController] and remembers it.
 */
@Composable
fun rememberCaptureController(): CaptureController {
    return remember { CaptureController() }
}