package io.validk

internal val REGEX_EMAIL = ".+@.+\\..+".toRegex()

fun <R : Number> Validation<R>.min(minValue: Number) = addConstraint("must be at least $minValue") {
    it.toDouble() >= minValue.toDouble()
}

fun <R : Number> Validation<R>.minExclusive(minValue: Number) = addConstraint("must be greater than $minValue") {
    it.toDouble() > minValue.toDouble()
}

fun <R : Number> Validation<R>.max(maxValue: Number) = addConstraint("must be at least $maxValue") {
    it.toDouble() <= maxValue.toDouble()
}

fun <R : Number> Validation<R>.maxExclusive(maxValue: Number) = addConstraint("must be less than $maxValue") {
    it.toDouble() < maxValue.toDouble()
}

fun Validation<String>.notEmptyString() = addConstraint("cannot be empty") {
    it.isNotEmpty()
}

fun Validation<String>.notBlank() = addConstraint("cannot be blank") {
    it.isNotBlank()
}

fun Validation<String>.minLength(minLength: Int) = addConstraint("must be at least $minLength characters") {
    it.length >= minLength
}

fun Validation<String>.maxLength(maxLength: Int) = addConstraint("must be at most $maxLength characters") {
    it.length <= maxLength
}

fun Validation<String>.enum(vararg values: String) = addConstraint("must be one of: ${values.joinToString(", ")}") {
    it in values
}

inline fun <reified T : Enum<T>> Validation<String>.enum() = enum(*enumValues<T>().map { it.name }.toTypedArray())

fun Validation<String>.matches(regex: Regex) = addConstraint("must match pattern ${regex.pattern}") {
    it.matches(regex)
}

fun Validation<String>.matches(pattern: String) = matches(pattern.toRegex())

fun Validation<String>.email() = matches(REGEX_EMAIL) message "must be a valid email"

fun Validation<out Collection<*>>.notEmpty() = addConstraint("cannot be empty") {
    it.isNotEmpty()
}

fun Validation<out Collection<*>>.minSize(minSize: Int) = addConstraint("should have at least ${minSize} elements") {
    it.size >= minSize
}

fun Validation<out Collection<*>>.maxSize(maxSize: Int) = addConstraint("should have at most ${maxSize} elements") {
    it.size <= maxSize
}


