package io.validk

interface ValidObject<T> {

    fun validation(): Validation<T>

    @Suppress("UNCHECKED_CAST")
    fun validate(): ValidationErrors? {
        return validation().validate(this as T)
    }
}

