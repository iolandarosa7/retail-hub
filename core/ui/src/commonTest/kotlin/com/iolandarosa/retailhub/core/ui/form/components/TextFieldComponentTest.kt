/*
 *
 * @Copyright 2026 Iolanda Rosa
 *
 */

package com.iolandarosa.retailhub.core.ui.form.components

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import com.iolandarosa.retailhub.core.ui.form.FakeValidator
import com.iolandarosa.retailhub.core.ui.form.fields.TextFormField
import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.error_test
import retailhub.core.ui.generated.resources.test_label
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class TextFieldComponentTest {
    private val expectedLabel = "Test Label"

    private fun setupField(
        initialValue: String = "",
        onValueChanged: (String?) -> Unit = {},
        validator: FakeValidator = FakeValidator(isValid = true),
    ) = TextFormField(
        name = "testField",
        labelResId = Res.string.test_label,
        initialValue = initialValue,
        onValueChange = onValueChanged,
        validators = listOf(validator),
    )

    @Test
    fun textFieldComponent_rendersLabel() =
        runComposeUiTest {
            setContent {
                FormFieldRenderer(setupField())
            }

            onNodeWithText(expectedLabel).assertIsDisplayed()
        }

    @Test
    fun textFieldComponent_rendersInitialValue() =
        runComposeUiTest {
            val initialValue = "John"

            setContent {
                FormFieldRenderer(setupField(initialValue = initialValue))
            }

            onNodeWithText(initialValue).assertIsDisplayed()
        }

    @Test
    fun textFieldComponent_changingText_updatesFieldAndCallsOnValueChanged() =
        runComposeUiTest {
            var callbackValue: String? = null

            val field =
                setupField(
                    onValueChanged = { value -> callbackValue = value },
                )
            val textInput = "Hello"

            setContent {
                TextFormFieldComponent(field)
            }

            onNodeWithText(expectedLabel)
                .assertIsDisplayed()
                .performTextInput(textInput)

            assertEquals(textInput, field.value)
            assertEquals(textInput, callbackValue)
        }

    @Test
    fun textFieldComponent_changingText_clearsError() =
        runComposeUiTest {
            val field =
                setupField(
                    validator = FakeValidator(isValid = false, messageId = Res.string.error_test),
                )

            field.validate()

            val textInput = "Hello"
            val expectedError = "Error"

            setContent {
                TextFormFieldComponent(field)
            }

            onNodeWithText(expectedLabel).assertIsDisplayed()
            onNodeWithText(expectedError)
                .assertIsDisplayed()
                .performTextInput(textInput)

            onNodeWithText(expectedError)
                .assertDoesNotExist()
        }
}
