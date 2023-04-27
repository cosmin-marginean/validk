package io.validk

data class ValidationErrors(val errors: List<ValidationError>) {

    constructor(propertyPath: String, errorMessage: String) : this(listOf(ValidationError(propertyPath, errorMessage)))

    val eagerErrors: List<ValidationError> = errors.eagerErrors()

    val errorMessages: Map<String, List<String>> =
        errors.groupBy { it.propertyPath }
            .map { it.key to it.value.map { error -> error.errorMessage } }
            .toMap()

    val eagerErrorMessages: Map<String, String> = eagerErrors.associate { it.propertyPath to it.errorMessage }

    val failedProperties: Set<String> = errors.map { it.propertyPath }.toSet()
}
