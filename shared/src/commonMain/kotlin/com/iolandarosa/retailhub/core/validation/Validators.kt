package com.iolandarosa.retailhub.core.validation

sealed class ValidationResult {
    data object Valid : ValidationResult()
    data class Invalid(val error: ValidationError) : ValidationResult()
}

enum class ValidationError {
    REQUIRED,
    INVALID_EMAIL,
    TOO_SHORT
}

object Validators {
    fun validateRequired(value: String): ValidationResult {
        return if (value.isBlank()) {
            ValidationResult.Invalid(ValidationError.REQUIRED)
        } else {
            ValidationResult.Valid
        }
    }

    // You can add more like validateEmail, validatePasswordLength, etc.
}