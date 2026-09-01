/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.form.validators.strings

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RequiredTest {
    private val validator = Required()

    @Test
    fun acceptsNonBlankValue() {
        val validValues =
            listOf(
                "John",
                " John",
                "John ",
                "John Doe",
            )

        validValues.forEach { value ->
            assertTrue(
                Required().validate(value),
                "Expected '$value' to be valid",
            )
        }
    }

    @Test
    fun rejectsNullValue() {
        assertFalse(validator.validate(null))
    }

    @Test
    fun rejectsBlankValue() {
        val invalidValues =
            listOf(
                null,
                "",
                " ",
                "   ",
                "\t",
                "\n",
            )

        invalidValues.forEach { value ->
            assertFalse(
                Required().validate(value),
                "Expected '$value' to be invalid",
            )
        }
    }
}
