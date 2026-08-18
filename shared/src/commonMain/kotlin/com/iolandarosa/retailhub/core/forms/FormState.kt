package com.iolandarosa.retailhub.core.forms

class FormState(val fields: List<FormField<*>>) {
    fun isFormValid(): Boolean {
        var valid = true
        fields
            .forEach {
                it.validate()
                if (it.error != null) {
                    valid = false
                    return@forEach
                }
            }
        return valid
    }

    fun <T> getFieldDataByName(fieldName: String): T? {
        val field = fields.filterIsInstance<FormField<T>>().firstOrNull { it.name == fieldName }
        return field?.value
    }
}