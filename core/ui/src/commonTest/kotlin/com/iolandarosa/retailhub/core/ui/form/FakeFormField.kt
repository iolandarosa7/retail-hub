package com.iolandarosa.retailhub.core.ui.form

import com.iolandarosa.retailhub.core.ui.form.validators.Validator

internal class FakeFormField(
    override val name: String = "field",
    override val validators: List<Validator<String>> = emptyList(),
    initialValue: String? = null,
) : FormField<String>(
    name = name,
    validators = validators,
    initialValue = initialValue,
)