package com.iolandarosa.retailhub.core.ui.form.components

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import com.iolandarosa.retailhub.core.ui.form.fields.TextFormField
import retailhub.core.ui.generated.resources.Res
import retailhub.core.ui.generated.resources.test_label
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class FormFieldRendererTest {
    private val labelResId = Res.string.test_label
    private val expectedLabel = "Test Label"

    private val field = TextFormField(
        name = "testField",
        labelResId = labelResId
    )

    @Test
    fun textFormField_rendersTextFormFieldComponent() = runComposeUiTest {
        setContent {
            FormFieldRenderer(
                field = field,
                enabled = false
            )
        }

        onNodeWithText(expectedLabel).assertIsDisplayed()
    }

    @Test
    fun textFormFieldCanBeDisabled() = runComposeUiTest {
        setContent {
            FormFieldRenderer(
                field = field,
                enabled = false
            )
        }

        onNodeWithText(expectedLabel)
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }
}
