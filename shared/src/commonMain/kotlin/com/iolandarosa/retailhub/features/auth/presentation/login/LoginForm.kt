package com.iolandarosa.retailhub.features.auth.presentation.login

import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.iolandarosa.retailhub.core.ui.form.fields.TextFormField
import com.iolandarosa.retailhub.core.ui.form.validators.strings.Required
import retailhub.shared.generated.resources.Res
import retailhub.shared.generated.resources.password
import retailhub.shared.generated.resources.username

object LoginForm {
    const val USERNAME = "username"
    const val PASSWORD = "password"

    fun get(
        onActionDone: (KeyboardActionScope) -> Unit,
    ) = listOf(
        TextFormField(
            name = USERNAME,
            validators = listOf(Required()),
            labelResId = Res.string.username,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next)
        ),
        TextFormField(
            name = PASSWORD,
            validators = listOf(Required()),
            labelResId = Res.string.password,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = onActionDone),
            visualTransformation = PasswordVisualTransformation(),
        )
    )
}
