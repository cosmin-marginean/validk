package io.validk

fun errors(vararg errors: ValidationError): ValidationResult {
    return ValidationResult(errors.toList())
}
