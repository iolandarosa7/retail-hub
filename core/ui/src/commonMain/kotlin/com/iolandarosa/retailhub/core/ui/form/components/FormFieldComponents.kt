package com.iolandarosa.retailhub.core.ui.form.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.iolandarosa.retailhub.core.ui.form.FormField
import com.iolandarosa.retailhub.core.ui.form.fields.TextFormField
import org.jetbrains.compose.resources.stringResource

@Composable
fun FormFieldRenderer(
    field: FormField<*>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    when (field) {
        is TextFormField -> {
            TextFormFieldComponent(field, modifier, enabled)
        }
    }
}

@Composable
fun TextFormFieldComponent(
    field: TextFormField,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = field.value ?: "",
        onValueChange = {
            field.clearErrors()
            field.value = it
            field.onValueChange(it)
        },
        enabled = enabled,
        label = { Text(stringResource(field.labelResId)) },
        keyboardOptions = field.keyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                field.keyboardActions.onDone?.invoke(this)
            },
            onNext = { field.keyboardActions.onNext?.invoke(this) },
            onSearch = { field.keyboardActions.onSearch?.invoke(this) },
            onSend = { field.keyboardActions.onSend?.invoke(this) },
            onGo = { field.keyboardActions.onGo?.invoke(this) },
            onPrevious = { field.keyboardActions.onPrevious?.invoke(this) }
        ),
        isError = field.error != null,
        supportingText = {
            field.error?.let {
                Text(stringResource(it))
            }
        },
        visualTransformation = field.visualTransformation,
        modifier = modifier,
    )
}