@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "CANNOT_OVERRIDE_INVISIBLE_MEMBER")
@file:OptIn(ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)

package com.multiplatform.app.ui.custom


import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.InternalComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.LocalSystemTheme
import androidx.compose.ui.SystemTheme
import androidx.compose.ui.createSkiaLayer
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.interop.LocalLayerContainer
import androidx.compose.ui.interop.LocalUIViewController
import androidx.compose.ui.node.LayoutNode
import androidx.compose.ui.platform.*
import androidx.compose.ui.semantics.SemanticsOwner
import androidx.compose.ui.text.input.PlatformTextInputService
import androidx.compose.ui.uikit.*
import androidx.compose.ui.unit.*
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.useContents
import org.jetbrains.skiko.SkikoUIView
import org.jetbrains.skiko.TextActions
import org.jetbrains.skiko.ios.SkikoUITextInputTraits
import platform.CoreGraphics.CGPointMake
import platform.CoreGraphics.CGRectMake
import platform.Foundation.*
import platform.UIKit.*
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.math.roundToInt
import kotlin.native.runtime.GC
import kotlin.native.runtime.NativeRuntimeApi

private val uiContentSizeCategoryToFontScaleMap = mapOf(
    UIContentSizeCategoryExtraSmall to 0.8f,
    UIContentSizeCategorySmall to 0.85f,
    UIContentSizeCategoryMedium to 0.9f,
    UIContentSizeCategoryLarge to 1f, // default preference
    UIContentSizeCategoryExtraLarge to 1.1f,
    UIContentSizeCategoryExtraExtraLarge to 1.2f,
    UIContentSizeCategoryExtraExtraExtraLarge to 1.3f,

    // These values don't work well if they match scale shown by
    // Text Size control hint, because iOS uses non-linear scaling
    // calculated by UIFontMetrics, while Compose uses linear.
    UIContentSizeCategoryAccessibilityMedium to 1.4f, // 160% native
    UIContentSizeCategoryAccessibilityLarge to 1.5f, // 190% native
    UIContentSizeCategoryAccessibilityExtraLarge to 1.6f, // 235% native
    UIContentSizeCategoryAccessibilityExtraExtraLarge to 1.7f, // 275% native
    UIContentSizeCategoryAccessibilityExtraExtraExtraLarge to 1.8f, // 310% native

    // UIContentSizeCategoryUnspecified
)

fun TransparentComposeUIViewController(content: @Composable () -> Unit): UIViewController =
    TransparentComposeUIViewController(configure = {}, content = content)

fun TransparentComposeUIViewController(
    configure: ComposeUIViewControllerConfiguration.() -> Unit = {},
    content: @Composable () -> Unit,
): UIViewController =
    TransparentComposeWindow().apply {
        configuration = ComposeUIViewControllerConfiguration()
            .apply(configure)
        setContent(content)
    }

private class AttachedComposeContext(
    val composeLayer: TransparentComposeLayer,
    val skiaLayer: org.jetbrains.skiko.SkiaLayer,
    val view: SkikoUIView,
    val inputTraits: SkikoUITextInputTraits,
    val platform: androidx.compose.ui.platform.Platform,
) {
    fun dispose() {
        composeLayer.dispose()
        view.removeFromSuperview()
    }
}


@OptIn(InternalComposeApi::class, ExperimentalForeignApi::class, BetaInteropApi::class, ExperimentalComposeUiApi::class)
@ExportObjCClass
class TransparentComposeWindow @OverrideInit constructor() : UIViewController(
    nibName = null,
    bundle = null
) {

    internal lateinit var configuration: ComposeUIViewControllerConfiguration
    private val keyboardOverlapHeightState = mutableStateOf(0f)
    private var safeAreaState by mutableStateOf(PlatformInsets())
    private var layoutMarginsState by mutableStateOf(PlatformInsets())

    /*
     * Initial value is arbitarily chosen to avoid propagating invalid value logic
     * It's never the case in real usage scenario to reflect that in type system
     */
    private val interfaceOrientationState = mutableStateOf(
        InterfaceOrientation.Portrait,
    )

    private val systemTheme = mutableStateOf(
        traitCollection.userInterfaceStyle.asComposeSystemTheme(),
    )

    /*
     * On iOS >= 13.0 interfaceOrientation will be deduced from [UIWindowScene] of [UIWindow]
     * to which our [ComposeWindow] is attached.
     * It's never UIInterfaceOrientationUnknown, if accessed after owning [UIWindow] was made key and visible:
     * https://developer.apple.com/documentation/uikit/uiwindow/1621601-makekeyandvisible?language=objc
     */
    private val currentInterfaceOrientation: InterfaceOrientation?
        get() {
            // Flag for checking which API to use
            // Modern: https://developer.apple.com/documentation/uikit/uiwindowscene/3198088-interfaceorientation?language=objc
            // Deprecated: https://developer.apple.com/documentation/uikit/uiapplication/1623026-statusbarorientation?language=objc
            val supportsWindowSceneApi =
                NSProcessInfo.processInfo.operatingSystemVersion.useContents {
                    majorVersion >= 13
                }

            return if (supportsWindowSceneApi) {
                view.window?.windowScene?.interfaceOrientation?.let {
                    InterfaceOrientation.getByRawValue(it)
                }
            } else {
                InterfaceOrientation.getByRawValue(UIApplication.sharedApplication.statusBarOrientation)
            }
        }

    private val fontScale: Float
        get() {
            val contentSizeCategory =
                traitCollection.preferredContentSizeCategory ?: UIContentSizeCategoryUnspecified

            return uiContentSizeCategoryToFontScaleMap[contentSizeCategory] ?: 1.0f
        }

    private val density: Density
        get() = Density(attachedComposeContext?.skiaLayer?.contentScale ?: 1f, fontScale)

    private lateinit var content: @Composable () -> Unit

    private var attachedComposeContext: AttachedComposeContext? = null

    @OptIn(BetaInteropApi::class)
    private val keyboardVisibilityListener = object : NSObject() {

        @Suppress("unused")
        @ObjCAction
        fun keyboardWillShow(arg: NSNotification) {
            val keyboardInfo = arg.userInfo!!["UIKeyboardFrameEndUserInfoKey"] as NSValue
            val keyboardHeight = keyboardInfo.CGRectValue().useContents { size.height }
            val screenHeight = UIScreen.mainScreen.bounds.useContents { size.height }

            val composeViewBottomY = UIScreen.mainScreen.coordinateSpace.convertPoint(
                point = CGPointMake(0.0, view.frame.useContents { size.height }),
                fromCoordinateSpace = view.coordinateSpace,
            ).useContents { y }
            val bottomIndent = screenHeight - composeViewBottomY

            if (bottomIndent < keyboardHeight) {
                keyboardOverlapHeightState.value = (keyboardHeight - bottomIndent).toFloat()
            }

            val composeLayer = attachedComposeContext?.composeLayer ?: return

            if (configuration.onFocusBehavior == OnFocusBehavior.FocusableAboveKeyboard) {
                val focusedRect = composeLayer.getActiveFocusRect()
                if (focusedRect != null) {
                    updateViewBounds(
                        offsetY = calcFocusedLiftingY(focusedRect, keyboardHeight),
                    )
                }
            }
        }

        @Suppress("unused")
        @ObjCAction
        fun keyboardWillHide(arg: NSNotification) {
            keyboardOverlapHeightState.value = 0f
            if (configuration.onFocusBehavior == OnFocusBehavior.FocusableAboveKeyboard) {
                updateViewBounds(offsetY = 0.0)
            }
        }

        private fun calcFocusedLiftingY(focusedRect: DpRect, keyboardHeight: Double): Double {
            val viewHeight = attachedComposeContext?.view?.frame?.useContents {
                size.height
            } ?: 0.0

            val hiddenPartOfFocusedElement: Double =
                keyboardHeight - viewHeight + focusedRect.bottom.value
            return if (hiddenPartOfFocusedElement > 0) {
                // If focused element is partially hidden by the keyboard, we need to lift it upper
                val focusedTopY = focusedRect.top.value
                val isFocusedElementRemainsVisible = hiddenPartOfFocusedElement < focusedTopY
                if (isFocusedElementRemainsVisible) {
                    // We need to lift focused element to be fully visible
                    hiddenPartOfFocusedElement
                } else {
                    // In this case focused element height is bigger than remain part of the screen after showing the keyboard.
                    // Top edge of focused element should be visible. Same logic on Android.
                    maxOf(focusedTopY, 0f).toDouble()
                }
            } else {
                // Focused element is not hidden by the keyboard.
                0.0
            }
        }

        private fun updateViewBounds(offsetX: Double = 0.0, offsetY: Double = 0.0) {
            val (width, height) = getViewFrameSize()
            view.layer.setBounds(
                CGRectMake(
                    x = offsetX,
                    y = offsetY,
                    width = width.toDouble(),
                    height = height.toDouble(),
                ),
            )
        }
    }

    @OptIn(BetaInteropApi::class)
    @Suppress("unused")
    @ObjCAction
    fun viewSafeAreaInsetsDidChange() {
        // super.viewSafeAreaInsetsDidChange() // TODO: call super after Kotlin 1.8.20
        view.safeAreaInsets.useContents {
            safeAreaState = PlatformInsets(
                top = top.dp,
                bottom = bottom.dp,
                left = left.dp,
                right = right.dp,
            )
        }
        view.directionalLayoutMargins.useContents {
            layoutMarginsState = PlatformInsets(
                top = top.dp,
                bottom = bottom.dp,
                left = leading.dp,
                right = trailing.dp,
            )
        }
    }

    override fun loadView() {
        view = UIView().apply {
            backgroundColor = UIColor.clearColor
            opaque = false
            //            backgroundColor = UIColor.whiteColor
            setClipsToBounds(true)
        } // rootView needs to interop with UIKit
    }

    override fun traitCollectionDidChange(previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)

        systemTheme.value = traitCollection.userInterfaceStyle.asComposeSystemTheme()
    }

    override fun viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()

        // UIKit possesses all required info for layout at this point
        currentInterfaceOrientation?.let {
            interfaceOrientationState.value = it
        }

        val composeLayer = attachedComposeContext?.composeLayer ?: return

        val (width, height) = getViewFrameSize()
        val scale = density.density

        composeLayer.setDensity(density)
        composeLayer.setSize((width * scale).roundToInt(), (height * scale).roundToInt())
    }

    override fun viewWillAppear(animated: Boolean) {
        super.viewWillAppear(animated)

        attachComposeIfNeeded()
    }

    override fun viewDidAppear(animated: Boolean) {
        super.viewDidAppear(animated)
        NSNotificationCenter.defaultCenter.addObserver(
            observer = keyboardVisibilityListener,
            selector = NSSelectorFromString(keyboardVisibilityListener::keyboardWillShow.name + ":"),
            name = UIKeyboardWillShowNotification,
            `object` = null,
        )
        NSNotificationCenter.defaultCenter.addObserver(
            observer = keyboardVisibilityListener,
            selector = NSSelectorFromString(keyboardVisibilityListener::keyboardWillHide.name + ":"),
            name = UIKeyboardWillHideNotification,
            `object` = null,
        )
    }

    // viewDidUnload() is deprecated and not called.
    override fun viewWillDisappear(animated: Boolean) {
        super.viewWillDisappear(animated)

        NSNotificationCenter.defaultCenter.removeObserver(
            observer = keyboardVisibilityListener,
            name = UIKeyboardWillShowNotification,
            `object` = null,
        )
        NSNotificationCenter.defaultCenter.removeObserver(
            observer = keyboardVisibilityListener,
            name = UIKeyboardWillHideNotification,
            `object` = null,
        )
    }

    @OptIn(NativeRuntimeApi::class)
    override fun viewDidDisappear(animated: Boolean) {
        super.viewDidDisappear(animated)

        dispose()

        dispatch_async(dispatch_get_main_queue()) {
            GC.collect()
        }
    }

    @OptIn(NativeRuntimeApi::class)
    override fun didReceiveMemoryWarning() {
        println("didReceiveMemoryWarning")
        GC.collect()
        super.didReceiveMemoryWarning()
    }

    fun setContent(
        content: @Composable () -> Unit,
    ) {
        this.content = content
    }

    fun dispose() {
        attachedComposeContext?.dispose()
        attachedComposeContext = null
    }

    private fun attachComposeIfNeeded() {
        if (attachedComposeContext != null) {
            return // already attached
        }

        val skiaLayer = createSkiaLayer()
        val skikoUIView = SkikoUIView(
            skiaLayer = skiaLayer,
            pointInside = { point, _ ->
                val composeLayer = attachedComposeContext?.composeLayer

                if (composeLayer == null) {
                    false
                } else {
                    !composeLayer.hitInteropView(point, isTouchEvent = true)
                }
            },
            skikoUITextInputTrains = attachedComposeContext?.inputTraits ?: object : SkikoUITextInputTraits {},
        ).load()

        skikoUIView.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(skikoUIView)

        NSLayoutConstraint.activateConstraints(
            listOf(
                skikoUIView.leadingAnchor.constraintEqualToAnchor(view.leadingAnchor),
                skikoUIView.trailingAnchor.constraintEqualToAnchor(view.trailingAnchor),
                skikoUIView.topAnchor.constraintEqualToAnchor(view.topAnchor),
                skikoUIView.bottomAnchor.constraintEqualToAnchor(view.bottomAnchor),
            ),
        )

        val inputServices = SkikoUIKitTextInputService(
            showSoftwareKeyboard = {
                skikoUIView.showScreenKeyboard()
            },
            hideSoftwareKeyboard = {
                skikoUIView.hideScreenKeyboard()
            },
            updateView = {
                skikoUIView.setNeedsDisplay() // redraw on next frame
                platform.QuartzCore.CATransaction.flush() // clear all animations
                skikoUIView.reloadInputViews() // update input (like screen keyboard)
            },
            textWillChange = { skikoUIView.textWillChange() },
            textDidChange = { skikoUIView.textDidChange() },
            selectionWillChange = { skikoUIView.selectionWillChange() },
            selectionDidChange = { skikoUIView.selectionDidChange() },
        )
        val inputTraits = inputServices.skikoUITextInputTraits

        val platform = object :
            androidx.compose.ui.platform.Platform by androidx.compose.ui.platform.Platform.Empty {
            override val windowInfo = WindowInfoImpl().apply {
                isWindowFocused = true
            }
            override var dialogScrimBlendMode by mutableStateOf(BlendMode.SrcOver)
            override val focusManager = EmptyFocusManager
            override val textInputService: PlatformTextInputService = inputServices
            override val viewConfiguration =
                object : ViewConfiguration {
                    override val longPressTimeoutMillis: Long get() = 500
                    override val doubleTapTimeoutMillis: Long get() = 300
                    override val doubleTapMinTimeMillis: Long get() = 40

                    // this value is originating from iOS 16 drag behavior reverse-engineering
                    override val touchSlop: Float get() = with(density) { 10.dp.toPx() }
                }
            override fun accessibilityController(owner: SemanticsOwner) = object : AccessibilityController {
                override fun onSemanticsChange() = Unit
                override fun onLayoutChange(layoutNode: LayoutNode) = Unit
                override suspend fun syncLoop() = Unit
            }
            override val textToolbar = object : TextToolbar {
                override fun showMenu(
                    rect: Rect,
                    onCopyRequested: (() -> Unit)?,
                    onPasteRequested: (() -> Unit)?,
                    onCutRequested: (() -> Unit)?,
                    onSelectAllRequested: (() -> Unit)?,
                ) {
                    val skiaRect = with(density) {
                        org.jetbrains.skia.Rect.makeLTRB(
                            l = rect.left / density,
                            t = rect.top / density,
                            r = rect.right / density,
                            b = rect.bottom / density,
                        )
                    }
                    skikoUIView.showTextMenu(
                        targetRect = skiaRect,
                        textActions = object : TextActions {
                            override val copy: (() -> Unit)? = onCopyRequested
                            override val cut: (() -> Unit)? = onCutRequested
                            override val paste: (() -> Unit)? = onPasteRequested
                            override val selectAll: (() -> Unit)? = onSelectAllRequested
                        },
                    )
                }

                /**
                 * TODO on UIKit native behaviour is hide text menu, when touch outside
                 */
                override fun hide() = skikoUIView.hideTextMenu()

                override val status: TextToolbarStatus
                    get() = if (skikoUIView.isTextMenuShown()) {
                        TextToolbarStatus.Shown
                    } else {
                        TextToolbarStatus.Hidden
                    }
            }

            override val inputModeManager = DefaultInputModeManager(InputMode.Touch)
        }
        val composeLayer = TransparentComposeLayer(
            layer = skiaLayer,
            platform = platform,
            input = inputServices.skikoInput,
        )

        composeLayer.setContent(
            onPreviewKeyEvent = inputServices::onPreviewKeyEvent,
            content = {
                CompositionLocalProvider(
                    LocalLayerContainer provides view,
                    LocalUIViewController provides this,
                    LocalKeyboardOverlapHeightState provides keyboardOverlapHeightState,
                    LocalSafeArea provides safeAreaState,
                    LocalLayoutMargins provides layoutMarginsState,
                    LocalInterfaceOrientationState provides interfaceOrientationState,
                    LocalSystemTheme provides systemTheme.value,
                ) {
                    content()
                }
            },
        )

        attachedComposeContext =
            AttachedComposeContext(composeLayer, skiaLayer, skikoUIView, inputTraits, platform)
    }

    private fun getViewFrameSize(): IntSize {
        val (width, height) = view.frame().useContents { this.size.width to this.size.height }
        return IntSize(width.toInt(), height.toInt())
    }
}

private fun UIUserInterfaceStyle.asComposeSystemTheme(): SystemTheme {
    return when (this) {
        UIUserInterfaceStyle.UIUserInterfaceStyleLight -> SystemTheme.Light
        UIUserInterfaceStyle.UIUserInterfaceStyleDark -> SystemTheme.Dark
        else -> SystemTheme.Unknown
    }
}