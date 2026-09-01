/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.form

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FormStateTest {
    @Test
    fun formIsValidWhenAllFieldsHaveEmptyValidators() {
        val form =
            FormState(
                fields =
                    listOf(
                        FakeFormField(name = "name"),
                        FakeFormField(name = "email"),
                    ),
            )

        assertTrue(form.isFormValid())
    }

    @Test
    fun formIsValidWhenAllFieldsAreValid() {
        val validValidator = FakeValidator(isValid = true)

        val form =
            FormState(
                fields =
                    listOf(
                        FakeFormField(
                            name = "name",
                            validators = listOf(validValidator),
                        ),
                        FakeFormField(
                            name = "email",
                            validators = listOf(validValidator),
                        ),
                    ),
            )

        assertTrue(form.isFormValid())
    }

    @Test
    fun formIsInvalidWhenAFieldIsInvalid() {
        val invalidValidator = FakeValidator(isValid = false)

        val form =
            FormState(
                fields =
                    listOf(
                        FakeFormField(
                            name = "name",
                        ),
                        FakeFormField(
                            name = "email",
                            validators = listOf(invalidValidator),
                        ),
                    ),
            )

        assertFalse(form.isFormValid())
    }

    @Test
    fun returnsFieldValueWhenFieldExists() {
        val fieldName = "email"
        val expectedFieldValue = "john@example.com"

        val form =
            FormState(
                fields =
                    listOf(
                        FakeFormField(
                            name = "name",
                            initialValue = "John",
                        ),
                        FakeFormField(
                            name = fieldName,
                            initialValue = expectedFieldValue,
                        ),
                    ),
            )

        val result: String? = form.getFieldDataByName(fieldName)

        assertEquals(expectedFieldValue, result)
    }

    @Test
    fun returnsNullWhenFieldDoesNotExist() {
        val form =
            FormState(
                fields =
                    listOf(
                        FakeFormField(
                            name = "name",
                            initialValue = "John",
                        ),
                    ),
            )

        val result: String? = form.getFieldDataByName("email")

        assertNull(result)
    }
}
