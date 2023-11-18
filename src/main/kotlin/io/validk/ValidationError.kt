package io.validk

data class ValidationError(
    val propertyPath: String,
    val errorMessage: String
) {
    internal fun indexed(property: String, index: Int): ValidationError {
        return ValidationError(
            propertyPath = this.propertyPath.indexedProperty(property, index),
            errorMessage = errorMessage
        )
    }

    companion object {
        internal fun String.indexedProperty(property: String, index: Int): String {
            return this.replace("""${property}(\.|$)""".toRegex(), "${property}[$index]$1")
        }
    }
}