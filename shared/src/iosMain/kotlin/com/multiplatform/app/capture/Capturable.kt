package com.multiplatform.app.capture


import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.CacheDrawModifierNode

import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.renderComposeScene
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.skia.Bitmap


/**
 * Adds a capture-ability on the Composable which can draw Bitmap from the Composable component.
 *
 * Example usage:
 *
 * ```
 *  val captureController = rememberCaptureController()
 *  val uiScope = rememberCoroutineScope()
 *
 *  // The content to be captured in to Bitmap
 *  Column(
 *      modifier = Modifier.capturable(captureController),
 *  ) {
 *      // Composable content
 *  }
 *
 *  Button(onClick = {
 *      // Capture content
 *      val bitmapAsync = captureController.captureAsync()
 *      try {
 *          val bitmap = bitmapAsync.await()
 *          // Do something with `bitmap`.
 *      } catch (error: Throwable) {
 *          // Error occurred, do something.
 *      }
 *  }) { ... }
 * ```
 *
 * @param controller A [CaptureController] which gives control to capture the Composable content.
 */
@ExperimentalComposeUiApi
fun Modifier.capturable(controller: CaptureController): Modifier {
    return this then CapturableModifierNodeElement(controller)
}

/**
 * Modifier implementation of Capturable
 */
private data class CapturableModifierNodeElement(
    private val controller: CaptureController
) : ModifierNodeElement<CapturableModifierNode>() {
    override fun create(): CapturableModifierNode {
        return CapturableModifierNode(controller)
    }

    override fun update(node: CapturableModifierNode) {
        node.controller = controller
    }
}

/**
 * Capturable Modifier node which delegates task to the [CacheDrawModifierNode] for drawing
 * Composable UI to the Picture and then helping it to converting picture into a Bitmap.
 */
@Suppress("unused")
private class CapturableModifierNode(
    var controller: CaptureController
) : DelegatingNode(), DelegatableNode {


    override fun onAttach() {
        super.onAttach()
        println("onAttach")
        coroutineScope.launch {
            controller.captureRequests.collectLatest { request ->
                val completable = request.imageBitmapDeferred
                runCatching {
                    println("collectLatest")
                    val image = renderComposeScene(600, 600, content = request.content)
                    val bitmap = Bitmap.makeFromImage(image)
                    completable.complete(bitmap.asComposeImageBitmap())
                    println("collectLatest")
                }.onFailure {
                    completable.completeExceptionally(it)
                }
            }
        }
    }
}
