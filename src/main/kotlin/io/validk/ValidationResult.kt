package io.validk

data class ValidationResult(val errors: List<ValidationError>) {
    val success: Boolean = errors.isEmpty()
    val failedProperties: List<String> = errors.map { it.propertyPath }
    val eagerErrors: List<ValidationError> by lazy { errors.eagerErrors() }

    constructor(propertyPath: String, errorMessage: String) : this(listOf(ValidationError(propertyPath, errorMessage)))
}