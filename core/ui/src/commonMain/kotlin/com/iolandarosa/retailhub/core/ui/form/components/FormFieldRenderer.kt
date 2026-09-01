/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.form.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iolandarosa.retailhub.core.ui.form.FormField
import com.iolandarosa.retailhub.core.ui.form.fields.TextFormField

@Composable
fun FormFieldRenderer(
    field: FormField<*>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    when (field) {
        is TextFormField -> {
            TextFormFieldComponent(field, modifier, enabled)
        }
    }
}
