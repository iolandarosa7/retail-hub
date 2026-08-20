package com.iolandarosa.retailhub.core.ui.form.validators.strings

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RequiredTest {
    private val validator = Required()

    @Test
    fun `accepts non blank value`() {
        val validValues = listOf(
            "John",
            " John",
            "John ",
            "John Doe"
        )

        validValues.forEach { value ->
            assertTrue(
                Required().validate(value),
                "Expected '$value' to be valid"
            )
        }
    }

    @Test
    fun `rejects null value`() {
        assertFalse(validator.validate(null))
    }

    @Test
    fun `rejects blank value`() {
        val invalidValues = listOf(
            null,
            "",
            " ",
            "   ",
            "\t",
            "\n"
        )

        invalidValues.forEach { value ->
            assertFalse(
                Required().validate(value),
                "Expected '$value' to be invalid"
            )
        }
    }
}