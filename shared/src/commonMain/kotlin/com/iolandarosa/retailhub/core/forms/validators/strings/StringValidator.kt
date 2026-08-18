package com.iolandarosa.retailhub.core.forms.validators.strings

import com.iolandarosa.retailhub.core.forms.validators.Validator
import org.jetbrains.compose.resources.StringResource

abstract class StringValidator(override val messageId: StringResource) : Validator<String>(messageId)