package io.validk

fun errors(vararg errors: ValidationError): ValidationErrors {
    return ValidationErrors(errors.toList())
}
