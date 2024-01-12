@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "CANNOT_OVERRIDE_INVISIBLE_MEMBER")

package com.multiplatform.app.ui.custom

/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.runtime.Composable
import androidx.compose.ui.ComposeScene
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.toCompose
import androidx.compose.ui.platform.Platform
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.toDpRect
import kotlinx.coroutines.CoroutineDispatcher
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color
import org.jetbrains.skia.Point
import org.jetbrains.skiko.*
import androidx.compose.ui.input.key.KeyEvent as ComposeKeyEvent

internal class TransparentComposeLayer(
    internal val layer: SkiaLayer,
    platform: Platform,
    private val input: SkikoInput,
) {
    private var isDisposed = false

    // Should be set to an actual value by ComposeWindow implementation
    private var density = Density(1f)

    inner class ComponentImpl : SkikoView {
        override val input = this@TransparentComposeLayer.input

        override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
            canvas.clear(Color.TRANSPARENT)
            scene.render(canvas, nanoTime)
        }

        override fun onKeyboardEvent(event: SkikoKeyboardEvent) {
            if (isDisposed) return
            scene.sendKeyEvent(KeyEvent(event))
        }

        @OptIn(ExperimentalComposeUiApi::class)
        override fun onPointerEvent(event: SkikoPointerEvent) {
            if (supportsMultitouch) {
                onPointerEventWithMultitouch(event)
            } else {
                // macos and web don't work properly when using onPointerEventWithMultitouch
                onPointerEventNoMultitouch(event)
            }
        }

        @OptIn(ExperimentalComposeUiApi::class)
        private fun onPointerEventWithMultitouch(event: SkikoPointerEvent) {
            val scale = density.density

            scene.sendPointerEvent(
                eventType = event.kind.toCompose(),
                pointers = event.pointers.map {
                    ComposeScene.Pointer(
                        id = PointerId(it.id),
                        position = Offset(
                            x = it.x.toFloat() * scale,
                            y = it.y.toFloat() * scale,
                        ),
                        pressed = it.pressed,
                        type = it.device.toCompose(),
                        pressure = it.pressure.toFloat(),
                    )
                },
                timeMillis = event.timestamp,
                nativeEvent = event,
            )
        }

        private fun onPointerEventNoMultitouch(event: SkikoPointerEvent) {
            val scale = density.density
            scene.sendPointerEvent(
                eventType = event.kind.toCompose(),
                scrollDelta = event.getScrollDelta(),
                position = Offset(
                    x = event.x.toFloat() * scale,
                    y = event.y.toFloat() * scale,
                ),
                timeMillis = currentMillis(),
                type = PointerType.Mouse,
                nativeEvent = event,
            )
        }
    }

    private val view = ComponentImpl()

    init {
        layer.skikoView = view
    }

    private val scene = ComposeScene(
        coroutineContext = getMainDispatcher(),
        platform = platform,
        density = density,
        invalidate = layer::needRedraw,
    )

    fun setDensity(newDensity: Density) {
        density = newDensity
        scene.density = newDensity
    }

    fun dispose() {
        check(!isDisposed)
        layer.detach()
        scene.close()
        _initContent = null
        isDisposed = true
    }

    fun setSize(width: Int, height: Int) {
        scene.constraints = Constraints(maxWidth = width, maxHeight = height)

        layer.needRedraw()
    }

    fun getActiveFocusRect(): DpRect? {
        val focusRect = scene.mainOwner?.focusOwner?.getFocusRect() ?: return null
        return focusRect.toDpRect(density)
    }

    fun hitInteropView(point: Point, isTouchEvent: Boolean): Boolean =
        scene.mainOwner?.hitInteropView(
            pointerPosition = Offset(point.x * density.density, point.y * density.density),
            isTouchEvent = isTouchEvent,
        ) ?: false

    fun setContent(
        onPreviewKeyEvent: (ComposeKeyEvent) -> Boolean = { false },
        onKeyEvent: (ComposeKeyEvent) -> Boolean = { false },
        content: @Composable () -> Unit,
    ) {
        // If we call it before attaching, everything probably will be fine,
        // but the first composition will be useless, as we set density=1
        // (we don't know the real density if we have unattached component)
        _initContent = {
            scene.setContent(
                onPreviewKeyEvent = onPreviewKeyEvent,
                onKeyEvent = onKeyEvent,
                content = content,
            )
        }

        initContent()
    }

    private var _initContent: (() -> Unit)? = null

    private fun initContent() {
        // TODO: do we need isDisplayable on SkiaLyer?
        // if (layer.isDisplayable) {
        _initContent?.invoke()
        _initContent = null
        // }
    }
}

internal fun getMainDispatcher(): CoroutineDispatcher = SkikoDispatchers.Main

private fun currentMillis() = (currentNanoTime() / 1E6).toLong()

internal val supportsMultitouch: Boolean = true

internal fun SkikoPointerEvent.getScrollDelta(): Offset {
    return this.takeIf {
        it.kind == SkikoPointerEventKind.SCROLL
    }?.let {
        Offset(it.deltaX.toFloat(), it.deltaY.toFloat())
    } ?: Offset.Zero
}