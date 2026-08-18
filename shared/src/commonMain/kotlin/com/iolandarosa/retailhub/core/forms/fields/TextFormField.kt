package com.iolandarosa.retailhub.core.forms.fields

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.VisualTransformation
import com.iolandarosa.retailhub.core.forms.FormField
import com.iolandarosa.retailhub.core.forms.validators.Validator
import org.jetbrains.compose.resources.StringResource


open class TextFormField(
    name: String,
    validators: List<Validator<String>> = emptyList(),
    val labelResId: StringResource,
    val onValueChange: (String?) -> Unit = {},
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    val keyboardActions: KeyboardActions = KeyboardActions.Default,
    val visualTransformation: VisualTransformation = VisualTransformation.None,
    initialValue: String? = null,
) : FormField<String>(
    name = name,
    validators = validators,
    initialValue = initialValue,
)
