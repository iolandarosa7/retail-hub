package com.iolandarosa.retailhub.core.ui.form

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FormStateTest {
    @Test
    fun `form is valid when all fields have empty validators`() {
        val form = FormState(
            fields = listOf(
                FakeFormField(name = "name"),
                FakeFormField(name = "email")
            )
        )

        assertTrue(form.isFormValid())
    }

    @Test
    fun `form is valid when all fields are valid`() {
        val validValidator = FakeValidator(isValid = true)

        val form = FormState(
            fields = listOf(
                FakeFormField(
                    name = "name",
                    validators = listOf(validValidator)
                ),
                FakeFormField(
                    name = "email",
                    validators = listOf(validValidator)
                )
            )
        )

        assertTrue(form.isFormValid())
    }

    @Test
    fun `form is invalid when a field is invalid`() {
        val invalidValidator = FakeValidator(isValid = false)

        val form = FormState(
            fields = listOf(
                FakeFormField(
                    name = "name",
                ),
                FakeFormField(
                    name = "email",
                    validators = listOf(invalidValidator),
                )
            )
        )

        assertFalse(form.isFormValid())
    }

    @Test
    fun `returns field value when field exists`() {
        val fieldName = "email"
        val expectedFieldValue = "john@example.com"

        val form = FormState(
            fields = listOf(
                FakeFormField(
                    name = "name",
                    initialValue = "John"
                ),
                FakeFormField(
                    name = fieldName,
                    initialValue = expectedFieldValue
                )
            )
        )

        val result: String? = form.getFieldDataByName(fieldName)

        assertEquals(expectedFieldValue, result)
    }

    @Test
    fun `returns null when field does not exist`() {
        val form = FormState(
            fields = listOf(
                FakeFormField(
                    name = "name",
                    initialValue = "John"
                )
            )
        )

        val result: String? = form.getFieldDataByName("email")

        assertNull(result)
    }
}