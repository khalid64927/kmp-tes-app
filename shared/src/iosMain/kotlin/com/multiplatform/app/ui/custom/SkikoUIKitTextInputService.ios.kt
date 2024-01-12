package com.multiplatform.app.ui.custom

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.text.input.*
import kotlin.math.min
import org.jetbrains.skia.BreakIterator
import org.jetbrains.skiko.SkikoInput
import org.jetbrains.skiko.SkikoKey
import org.jetbrains.skiko.SkikoKeyboardEventKind
import org.jetbrains.skiko.ios.SkikoUITextInputTraits
import platform.UIKit.*

internal class SkikoUIKitTextInputService(
    showSoftwareKeyboard: () -> Unit,
    hideSoftwareKeyboard: () -> Unit,
    private val updateView: () -> Unit,
    private val textWillChange: () -> Unit,
    private val textDidChange: () -> Unit,
    private val selectionWillChange: () -> Unit,
    private val selectionDidChange: () -> Unit,
) : PlatformTextInputService {

    data class CurrentInput(
        var value: TextFieldValue,
        val onEditCommand: (List<EditCommand>) -> Unit
    )

    private val _showSoftwareKeyboard: () -> Unit = showSoftwareKeyboard
    private val _hideSoftwareKeyboard: () -> Unit = hideSoftwareKeyboard
    private var currentInput: CurrentInput? = null
    private var currentImeOptions: ImeOptions? = null
    private var currentImeActionHandler: ((ImeAction) -> Unit)? = null
    private var _tempCurrentInputSession: EditProcessor? = null
    private var _tempHardwareReturnKeyPressed: Boolean = false
    private var _tempImeActionIsCalledWithHardwareReturnKey: Boolean = false
    private var _tempCursorPos: Int? = null

    override fun startInput(
        value: TextFieldValue,
        imeOptions: ImeOptions,
        onEditCommand: (List<EditCommand>) -> Unit,
        onImeActionPerformed: (ImeAction) -> Unit
    ) {
        currentInput = CurrentInput(value, onEditCommand)
        _tempCurrentInputSession = EditProcessor().apply {
            reset(value, null)
        }
        currentImeOptions = imeOptions
        currentImeActionHandler = onImeActionPerformed
        showSoftwareKeyboard()
    }

    override fun stopInput() {
        currentInput = null
        _tempCurrentInputSession = null
        currentImeOptions = null
        currentImeActionHandler = null
        hideSoftwareKeyboard()
    }

    override fun showSoftwareKeyboard() {
        _showSoftwareKeyboard()
    }

    override fun hideSoftwareKeyboard() {
        _hideSoftwareKeyboard()
    }

    override fun updateState(oldValue: TextFieldValue?, newValue: TextFieldValue) {
        val internalOldValue = _tempCurrentInputSession?.toTextFieldValue()
        val textChanged = internalOldValue == null || internalOldValue.text != newValue.text
        val selectionChanged = textChanged || internalOldValue == null || internalOldValue.selection != newValue.selection
        if (textChanged) {
            textWillChange()
        }
        if (selectionChanged) {
            selectionWillChange()
        }
        _tempCurrentInputSession?.reset(newValue, null)
        currentInput?.let { input ->
            input.value = newValue
            _tempCursorPos = null
        }
        if (textChanged) {
            textDidChange()
        }
        if (selectionChanged) {
            selectionDidChange()
        }
        if (textChanged || selectionChanged) {
            updateView()
        }
    }

    val skikoInput = object : SkikoInput {

        override fun hasText(): Boolean = getState()?.text?.isNotEmpty() ?: false

        override fun insertText(text: String) {
            if (text == "\n") {
                if (runImeActionIfRequired()) {
                    return
                }
            }
            getCursorPos()?.let {
                _tempCursorPos = it + text.length
            }
            sendEditCommand(CommitTextCommand(text, 1))
        }

        override fun deleteBackward() {
            // Before this function calls, iOS changes selection in setSelectedTextRange.
            // All needed characters should be allready selected, and we can just remove them.
            sendEditCommand(
                CommitTextCommand("", 0)
            )
        }

        override fun endOfDocument(): Long = getState()?.text?.length?.toLong() ?: 0L

        override fun getSelectedTextRange(): IntRange? {
            val cursorPos = getCursorPos()
            if (cursorPos != null) {
                return cursorPos until cursorPos
            }
            val selection = getState()?.selection
            return if (selection != null) {
                selection.start until selection.end
            } else {
                null
            }
        }

        override fun setSelectedTextRange(range: IntRange?) {
            if (range != null) {
                sendEditCommand(
                    SetSelectionCommand(range.start, range.endInclusive + 1)
                )
            } else {
                sendEditCommand(
                    SetSelectionCommand(endOfDocument().toInt(), endOfDocument().toInt())
                )
            }
        }

        override fun selectAll() {
            sendEditCommand(
                SetSelectionCommand(0, endOfDocument().toInt())
            )
        }

        override fun textInRange(range: IntRange): String {
            val text = getState()?.text
            return text?.substring(range.first, min(range.last + 1, text.length)) ?: ""
        }

        override fun replaceRange(range: IntRange, text: String) {
            sendEditCommand(
                SetComposingRegionCommand(range.start, range.endInclusive + 1),
                SetComposingTextCommand(text, 1),
                FinishComposingTextCommand(),
            )
        }

        override fun setMarkedText(markedText: String?, selectedRange: IntRange) {
            if (markedText != null) {
                sendEditCommand(
                    SetComposingTextCommand(markedText, 1)
                )
            }
        }

        override fun markedTextRange(): IntRange? {
            val composition = getState()?.composition
            return if (composition != null) {
                composition.start until composition.end
            } else {
                null
            }
        }

        override fun unmarkText() {
            sendEditCommand(FinishComposingTextCommand())
        }
    }

    val skikoUITextInputTraits = object : SkikoUITextInputTraits {
        override fun keyboardType(): UIKeyboardType =
            when (currentImeOptions?.keyboardType) {
                KeyboardType.Text -> UIKeyboardTypeDefault
                KeyboardType.Ascii -> UIKeyboardTypeASCIICapable
                KeyboardType.Number -> UIKeyboardTypeNumberPad
                KeyboardType.Phone -> UIKeyboardTypePhonePad
                KeyboardType.Uri -> UIKeyboardTypeURL
                KeyboardType.Email -> UIKeyboardTypeEmailAddress
                KeyboardType.Password -> UIKeyboardTypeASCIICapable // TODO Correct?
                KeyboardType.NumberPassword -> UIKeyboardTypeNumberPad // TODO Correct?
                KeyboardType.Decimal -> UIKeyboardTypeDecimalPad
                else -> UIKeyboardTypeDefault
            }

        override fun keyboardAppearance(): UIKeyboardAppearance = UIKeyboardAppearanceDefault
        override fun returnKeyType(): UIReturnKeyType =
            when (currentImeOptions?.imeAction) {
                ImeAction.Default -> UIReturnKeyType.UIReturnKeyDefault
                ImeAction.None -> UIReturnKeyType.UIReturnKeyDefault
                ImeAction.Go -> UIReturnKeyType.UIReturnKeyGo
                ImeAction.Search -> UIReturnKeyType.UIReturnKeySearch
                ImeAction.Send -> UIReturnKeyType.UIReturnKeySend
                ImeAction.Previous -> UIReturnKeyType.UIReturnKeyDefault
                ImeAction.Next -> UIReturnKeyType.UIReturnKeyNext
                ImeAction.Done -> UIReturnKeyType.UIReturnKeyDone
                else -> UIReturnKeyType.UIReturnKeyDefault
            }

        override fun textContentType(): UITextContentType? = null
        //           TODO: Prevent Issue https://youtrack.jetbrains.com/issue/COMPOSE-319/iOS-Bug-password-TextField-changes-behavior-for-all-other-TextFieds
        //            when (currentImeOptions?.keyboardType) {
        //                KeyboardType.Password, KeyboardType.NumberPassword -> UITextContentTypePassword
        //                KeyboardType.Email -> UITextContentTypeEmailAddress
        //                KeyboardType.Phone -> UITextContentTypeTelephoneNumber
        //                else -> null
        //            }

        override fun isSecureTextEntry(): Boolean = false
        //           TODO: Prevent Issue https://youtrack.jetbrains.com/issue/COMPOSE-319/iOS-Bug-password-TextField-changes-behavior-for-all-other-TextFieds
        //            when (currentImeOptions?.keyboardType) {
        //                KeyboardType.Password, KeyboardType.NumberPassword -> true
        //                else -> false
        //            }

        override fun enablesReturnKeyAutomatically(): Boolean = false

        override fun autocapitalizationType(): UITextAutocapitalizationType =
            when (currentImeOptions?.capitalization) {
                KeyboardCapitalization.None ->
                    UITextAutocapitalizationType.UITextAutocapitalizationTypeNone

                KeyboardCapitalization.Characters ->
                    UITextAutocapitalizationType.UITextAutocapitalizationTypeAllCharacters

                KeyboardCapitalization.Words ->
                    UITextAutocapitalizationType.UITextAutocapitalizationTypeWords

                KeyboardCapitalization.Sentences ->
                    UITextAutocapitalizationType.UITextAutocapitalizationTypeSentences

                else ->
                    UITextAutocapitalizationType.UITextAutocapitalizationTypeNone
            }

        override fun autocorrectionType(): UITextAutocorrectionType =
            when (currentImeOptions?.autoCorrect) {
                true -> UITextAutocorrectionType.UITextAutocorrectionTypeYes
                false -> UITextAutocorrectionType.UITextAutocorrectionTypeNo
                else -> UITextAutocorrectionType.UITextAutocorrectionTypeDefault
            }

    }

    fun onPreviewKeyEvent(event: KeyEvent): Boolean {
        val nativeKeyEvent = event.nativeKeyEvent
        return when (nativeKeyEvent.key) {
            SkikoKey.KEY_ENTER -> handleEnterKey(nativeKeyEvent)
            SkikoKey.KEY_BACKSPACE -> handleBackspace(nativeKeyEvent)
            else -> false
        }
    }

    private fun handleEnterKey(event: NativeKeyEvent): Boolean {
        _tempImeActionIsCalledWithHardwareReturnKey = false
        return when (event.kind) {
            SkikoKeyboardEventKind.UP -> {
                _tempHardwareReturnKeyPressed = false
                false
            }

            SkikoKeyboardEventKind.DOWN -> {
                _tempHardwareReturnKeyPressed = true
                // This prevents two new line characters from being added for one hardware return key press.
                true
            }

            else -> false
        }
    }

    private fun handleBackspace(event: NativeKeyEvent): Boolean {
        // This prevents two characters from being removed for one hardware backspace key press.
        return event.kind == SkikoKeyboardEventKind.DOWN
    }

    private fun sendEditCommand(vararg commands: EditCommand) {
        val commandList = commands.toList()
        _tempCurrentInputSession?.apply(commandList)
        currentInput?.let { input ->
            input.onEditCommand(commandList)
        }
    }

    private fun getCursorPos(): Int? {
        if (_tempCursorPos != null) {
            return _tempCursorPos
        }
        val selection = getState()?.selection
        if (selection != null && selection.start == selection.end) {
            return selection.start
        }
        return null
    }

    private fun imeActionRequired(): Boolean =
        currentImeOptions?.run {
            singleLine || (
                    imeAction != ImeAction.None
                            && imeAction != ImeAction.Default
                            && !(imeAction == ImeAction.Search && _tempHardwareReturnKeyPressed)
                    )
        } ?: false

    private fun runImeActionIfRequired(): Boolean {
        val imeAction = currentImeOptions?.imeAction ?: return false
        val imeActionHandler = currentImeActionHandler ?: return false
        if (!imeActionRequired()) {
            return false
        }
        if (!_tempImeActionIsCalledWithHardwareReturnKey) {
            if (imeAction == ImeAction.Default) {
                imeActionHandler(ImeAction.Done)
            } else {
                imeActionHandler(imeAction)
            }
        }
        if (_tempHardwareReturnKeyPressed) {
            _tempImeActionIsCalledWithHardwareReturnKey = true
        }
        return true
    }

    private fun getState(): TextFieldValue? = currentInput?.value

}

private fun UITextGranularity.toTextIterator() =
    when (this) {
        UITextGranularity.UITextGranularitySentence -> BreakIterator.makeSentenceInstance()
        UITextGranularity.UITextGranularityLine -> BreakIterator.makeLineInstance()
        UITextGranularity.UITextGranularityWord -> BreakIterator.makeWordInstance()
        UITextGranularity.UITextGranularityCharacter -> BreakIterator.makeCharacterInstance()
        UITextGranularity.UITextGranularityParagraph -> TODO("UITextGranularityParagraph iterator")
        UITextGranularity.UITextGranularityDocument -> TODO("UITextGranularityDocument iterator")
        else -> error("Unknown granularity")
    }