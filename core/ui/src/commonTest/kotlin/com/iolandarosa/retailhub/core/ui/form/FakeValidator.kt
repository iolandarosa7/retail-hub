/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.form

import com.iolandarosa.retailhub.core.ui.form.validators.Validator
import org.jetbrains.compose.resources.StringResource
import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.error_test

internal class FakeValidator(
    val isValid: Boolean,
    override val messageId: StringResource = Res.string.error_test,
) : Validator<String>(messageId) {
    override fun validate(value: String?): Boolean = isValid
}
