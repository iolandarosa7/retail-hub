/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.form.validators

import org.jetbrains.compose.resources.StringResource

abstract class Validator<T>(
    open val messageId: StringResource,
) {
    abstract fun validate(value: T?): Boolean

    override fun equals(other: Any?): Boolean {
        if (other !is Validator<*>) {
            return false
        }

        return messageId == other.messageId
    }

    override fun hashCode(): Int = messageId.hashCode()
}
