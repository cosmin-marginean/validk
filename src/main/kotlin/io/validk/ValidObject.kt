package io.validk

interface ValidObject<T> {

    fun validation(): Validation<T>

    @Suppress("UNCHECKED_CAST")
    fun validate(): ValidationResult {
        return validation().validate(this as T)
    }
}

