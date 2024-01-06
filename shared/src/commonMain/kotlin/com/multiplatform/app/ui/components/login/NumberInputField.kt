package com.multiplatform.app.ui.components.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.multiplatform.app.ui.theme.prepaidErrorColor
import com.multiplatform.app.ui.theme.prepaidIndigoDark
import com.multiplatform.app.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun NumberInputField(
    query: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    maxCharLimit: Int,
    validationError: Boolean
) {

    val keyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done
    )
    val textState = remember { mutableStateOf(query) }
    val limitedValue = rememberUpdatedState(textState.value)
    OutlinedTextField(
        maxLines = 1,
        value = limitedValue.value,
        label = { Text(text =  stringResource(MR.strings.login_hint)) },
        keyboardOptions = keyboardOptions,
        onValueChange = {
            if (it.length <= maxCharLimit) {
                textState.value = it
                onValueChange(it)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp, end = 0.dp, start = 0.dp),
        enabled = true,
        colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = MaterialTheme.colorScheme.onSurface,
            errorBorderColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = if(validationError) prepaidErrorColor else prepaidIndigoDark,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
    )
}