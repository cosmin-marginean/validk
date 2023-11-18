package io.validk

interface ValidObject<T> {

    val validation: Validation<T>

    @Suppress("UNCHECKED_CAST")
    fun validate(): ValidationErrors? {
        return validation.validate(this as T)
    }

    fun <R> validate(block: ValidationCheck<R>.() -> Unit): R {
        val builder = ValidationCheck<R>()
        block(builder)
        val errors = validate()
        return if (errors != null) {
            builder.onError!!(errors)
        } else {
            builder.onSuccess!!()
        }
    }
}

class ValidationCheck<R>(
    var onError: ((ValidationErrors) -> R)? = null,
    var onSuccess: (() -> R)? = null
) {
    fun onError(onError: (ValidationErrors) -> R) {
        this.onError = onError
    }

    fun onSuccess(onSuccess: () -> R) {
        this.onSuccess = onSuccess
    }
}
