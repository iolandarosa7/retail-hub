package com.iolandarosa.retailhub.core.forms.validators.strings

import org.jetbrains.compose.resources.StringResource
import retailhub.shared.generated.resources.Res
import retailhub.shared.generated.resources.error_email_format

class Email(
    messageId: StringResource = Res.string.error_email_format,
    private val regex: Regex = EMAIL_REGEX,
) : StringValidator(messageId) {

    override fun validate(value: String?): Boolean {
        if (value.isNullOrEmpty()) {
            return true
        }

        return regex.matches(value)
    }

    companion object {
        val EMAIL_REGEX = Regex(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                    ")+"
        )
    }
}