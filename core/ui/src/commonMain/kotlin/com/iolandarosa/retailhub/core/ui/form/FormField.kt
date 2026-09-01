/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.form

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.iolandarosa.retailhub.core.ui.form.validators.Validator
import org.jetbrains.compose.resources.StringResource

@Stable
abstract class FormField<T>(
    open val name: String,
    open val validators: List<Validator<T>>,
    val initialValue: T? = null,
) {
    var value: T? by mutableStateOf(initialValue)
    var error: StringResource? by mutableStateOf(null)

    private fun showError(messageId: StringResource) {
        error = messageId
    }

    fun clearErrors() {
        error = null
    }

    fun validate() {
        validators.forEach {
            val isValid = it.validate(value)
            if (!isValid) {
                showError(it.messageId)
                return
            }

            clearErrors()
        }
    }
}
