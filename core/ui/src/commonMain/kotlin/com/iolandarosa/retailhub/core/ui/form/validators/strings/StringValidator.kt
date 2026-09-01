/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.form.validators.strings

import com.iolandarosa.retailhub.core.ui.form.validators.Validator
import org.jetbrains.compose.resources.StringResource

abstract class StringValidator(
    override val messageId: StringResource,
) : Validator<String>(messageId)
