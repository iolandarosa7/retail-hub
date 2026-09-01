/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.form.validators.strings

import org.jetbrains.compose.resources.StringResource
import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.error_required_field

class Required(
    messageId: StringResource = Res.string.error_required_field,
) : StringValidator(messageId) {
    override fun validate(value: String?): Boolean = !value.isNullOrBlank()
}
