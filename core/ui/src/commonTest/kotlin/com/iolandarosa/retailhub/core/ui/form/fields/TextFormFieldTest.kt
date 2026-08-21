package com.iolandarosa.retailhub.core.ui.form.fields

import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.error_unknown
import kotlin.test.Test
import kotlin.test.assertEquals

class TextFormFieldTest {

    @Test
    fun initializesWithProvidedNameAndValue() {
        val field = TextFormField(
            name = "email",
            labelResId = Res.string.error_unknown,
            initialValue = "test@example.com",
        )

        assertEquals("email", field.name)
        assertEquals("test@example.com", field.value)
    }

    @Test
    fun onValueChangeReceivesChangedValue() {
        var changedValue: String? = null

        val field = TextFormField(
            name = "email",
            labelResId = Res.string.error_unknown,
            onValueChange = { changedValue = it },
        )

        field.onValueChange("new@example.com")

        assertEquals("new@example.com", changedValue)
    }
}