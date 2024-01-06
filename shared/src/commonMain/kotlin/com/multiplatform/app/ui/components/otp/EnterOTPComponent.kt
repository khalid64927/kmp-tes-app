package com.multiplatform.app.ui.components.otp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multiplatform.app.ui.theme.prepaidIndigoDark


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LazyItemScope.otpComponent(
    onOtpEntered: (String) -> Unit,
    onOtpSubmit: (String) -> Unit,
    otpString: String
) {
    var otp by remember { mutableStateOf(Array(6) { "" }) }
    // Initialize the otp array with empty strings
    for (i in 0 until minOf(otp.size, otpString.length)) {
        otp[i] = otpString.getOrNull(i)?.toString() ?: ""
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // Initialize an array of FocusRequesters
        val focusRequesters = remember { Array(6) { FocusRequester() } }
        otp.forEachIndexed { index, value ->

            val textState = remember { mutableStateOf(value) }
            val limitedValue = rememberUpdatedState(textState.value)

            // Initialize a SoftwareKeyboardController
            val softwareKeyboardController = LocalSoftwareKeyboardController.current

            OutlinedTextField(
                value = limitedValue.value,
                onValueChange = { newValue ->
                    if (newValue.length == 1 ) {
                        otp[index] = newValue
                        if(index < otp.size - 1){
                            otp[index + 1] = ""
                            focusRequesters[index + 1].requestFocus()

                        }
                    }

                    textState.value = otp[index]
                    onOtpEntered(otp.joinToString(""))
                    if(index == otp.size - 1) {
                        onOtpSubmit(otp.joinToString(""))
                        softwareKeyboardController?.hide()
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (index == otp.size - 1) ImeAction.Done else ImeAction.Next
                ),
                textStyle = TextStyle.Default.copy(color = Color.Black, fontSize = 18.sp),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequesters[index])
                    .fillMaxWidth()
                    .height(83.dp)
                    .padding(0.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = MaterialTheme.colorScheme.onSurface,
                    errorBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = prepaidIndigoDark,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )


            )

        }
    }

}
