/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.features.auth.login

import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.iolandarosa.retailhub.core.ui.form.fields.TextFormField
import com.iolandarosa.retailhub.core.ui.form.validators.strings.Required
import retailhub.features.auth.generated.resources.Res
import retailhub.features.auth.generated.resources.password
import retailhub.features.auth.generated.resources.username

object LoginForm {
    const val USERNAME = "username"
    const val PASSWORD = "password"

    fun get(
        onValueChanged: (String?) -> Unit,
        onActionDone: (KeyboardActionScope) -> Unit,
    ) = listOf(
        TextFormField(
            name = USERNAME,
            validators = listOf(Required()),
            labelResId = Res.string.username,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
            onValueChange = onValueChanged,
        ),
        TextFormField(
            name = PASSWORD,
            validators = listOf(Required()),
            labelResId = Res.string.password,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = onActionDone),
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = onValueChanged,
        ),
    )
}
