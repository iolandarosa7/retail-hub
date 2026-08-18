package com.iolandarosa.retailhub.core.forms.validators.strings

import org.jetbrains.compose.resources.StringResource
import retailhub.shared.generated.resources.Res
import retailhub.shared.generated.resources.error_required_field

class Required(
    messageId: StringResource = Res.string.error_required_field,
) : StringValidator(messageId) {

    override fun validate(value: String?): Boolean = !value.isNullOrBlank()
}