package io.validk

class Constraint<T>(
    var errorMessage: String,
    val test: (T) -> Boolean
)