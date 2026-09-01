/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.form

import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.error_test
import retailhub.core.ui.generated.resources.error_test_variant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FormFieldTest {
    @Test
    fun initializesWithGivenValue() {
        val field = FakeFormField(initialValue = "John")

        assertEquals("John", field.value)
        assertNull(field.error)
    }

    @Test
    fun validateClearsErrorWhenValueIsValid() {
        val validator = FakeValidator(isValid = true)

        val field = FakeFormField(validators = listOf(validator))

        field.error = validator.messageId

        field.validate()

        assertNull(field.error)
    }

    @Test
    fun validateSetsErrorWhenValueIsInvalid() {
        val validator = FakeValidator(isValid = false)

        val field = FakeFormField(validators = listOf(validator))

        field.validate()

        assertEquals(validator.messageId, field.error)
    }

    @Test
    fun validateStopsAtFirstInvalidValidator() {
        val firstValidator = FakeValidator(isValid = false)

        val secondValidator =
            FakeValidator(
                isValid = true,
                messageId = Res.string.error_test_variant,
            )

        val field =
            FakeFormField(
                validators =
                    listOf(
                        firstValidator,
                        secondValidator,
                    ),
            )

        field.validate()

        assertEquals(firstValidator.messageId, field.error)
    }

    @Test
    fun clearErrorsRemovesCurrentError() {
        val field = FakeFormField()

        field.error = Res.string.error_test

        field.clearErrors()

        assertNull(field.error)
    }
}
