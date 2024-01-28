package io.validk

import kotlin.reflect.KProperty1

class Validation<T>(
    private val propertyPath: String? = null,
    private val nullMessage: String = DEFAULT_NULL_MESSAGE,
    private val checksCollectionElements: Boolean = false,
    private val failFast: Boolean = true
) {
    private val constraints = mutableListOf<Constraint<T>>()
    private val childValidations = mutableMapOf<KProperty1<T, Any?>, Validation<Any>>()
    private val dynamicValidations = mutableListOf<DynamicValidation<T>>()

    fun addConstraint(errorMessage: String, test: (T) -> Boolean): Constraint<T> {
        val constraint = Constraint(errorMessage, test)
        constraints.add(constraint)
        return constraint
    }

    infix fun <R> Constraint<R>.message(errorMessage: String): Constraint<R> {
        this.errorMessage = errorMessage
        return this
    }

    operator fun <R> KProperty1<T, R>.invoke(init: Validation<R>.() -> Unit) {
        addChildValidation(this, init)
    }

    private fun <E> addChildValidation(
        property: KProperty1<T, Any?>,
        init: Validation<E>.() -> Unit,
        nullMessage: String = DEFAULT_NULL_MESSAGE,
        checksCollectionElements: Boolean = false,
    ): Validation<E> {
        val validation = Validation<E>(propertyPath.plusChildProperty(property.name), nullMessage, checksCollectionElements, failFast)
        init(validation)
        childValidations[property] = validation as Validation<Any>
        return validation
    }

    fun <R> KProperty1<T, R>.whenIs(value: R, block: Validation<T>.(T) -> Unit) {
        val property = this
        dynamicValidations.add {
            if (property.get(it) == value) {
                block(it)
            }
        }
    }

    fun <E> KProperty1<T, Collection<E>>.whenContains(value: E, block: Validation<T>.(T) -> Unit) {
        val property = this
        dynamicValidations.add {
            if (property.get(it).contains(value)) {
                block(it)
            }
        }
    }

    infix fun <R> KProperty1<T, R?>.ifNotNull(block: Validation<R>.() -> Unit) {
        val property = this
        dynamicValidations.add {
            if (property.get(it) != null) {
                addChildValidation(property as KProperty1<T, R>, block)
            }
        }
    }

    infix fun <R> KProperty1<T, R?>.notNull(block: Validation<R>.() -> Unit) {
        notNull(DEFAULT_NULL_MESSAGE, block)
    }

    fun <R> KProperty1<T, R?>.notNull(errorMessage: String, block: Validation<R>.() -> Unit) {
        val property = this
        addChildValidation<R>(property, {}, errorMessage).apply {
            property.ifNotNull(block)
        }
    }

    infix fun <E, R : Collection<E>> KProperty1<T, R>.each(block: Validation<E>.() -> Unit) {
        addChildValidation(this, block, checksCollectionElements = true)
    }

    fun withValue(block: Validation<T>.(T) -> Unit) {
        dynamicValidations.add(block)
    }

    private fun String?.plusChildProperty(property: String): String {
        return this?.let { "${it}.${property}" } ?: property
    }

    fun validate(value: T?): ValidationErrors? {
        if (value == null) {
            // Technically, we would not have a Validation<T> unless:
            // A) val pojo: T is non-nullable (and we added validations against it)
            // or
            // B) val pojo: T? is nullable, but we added a Validation for it (via notNull or ifNotNull)
            //
            // In either case, this value should not be null and if it is, then we should fail the validation for it now
            // as none of the nested logic/constraints can be applied anyway
            return ValidationErrors(propertyPath!!, nullMessage)
        }

        val errors = mutableListOf<ValidationError>()

        if (failFast) {
            run breaking@{
                constraints.forEach { constraint ->
                    testConstraint(constraint, value, errors)
                    if (errors.size == 1) return@breaking
                }
            }
        } else {
            constraints.forEach { constraint ->
                testConstraint(constraint, value, errors)
            }
        }

        if (dynamicValidations.isNotEmpty()) {
            val dynamicValidation = Validation<T>(propertyPath = propertyPath, failFast = failFast)
            dynamicValidations.forEach { valueValidation ->
                valueValidation(dynamicValidation, value)
            }
            dynamicValidation.validate(value)?.let { errors.addAll(it.errors) }
        }

        childValidations.forEach { (property, validation) ->
            val propertyValue = property.get(value)
            if (validation.checksCollectionElements) {
                (propertyValue as Collection<*>).forEachIndexed { index, element ->
                    validation.validate(element!!)?.let { errors.addAll(it.errors.map { it.indexed(property.name, index) }) }
                }
            } else {
                validation.validate(propertyValue)?.let { errors.addAll(it.errors) }
            }
        }

        return if (errors.isNotEmpty()) {
            ValidationErrors(errors)
        } else {
            null
        }
    }

    private fun testConstraint(
        constraint: Constraint<T>,
        value: T,
        errors: MutableList<ValidationError>
    ) {
        if (!constraint.test(value)) {
            val property = propertyPath ?: "Object"
            errors.add(ValidationError(property, constraint.errorMessage))
        }
    }

    companion object {
        const val DEFAULT_NULL_MESSAGE = "is required"

        operator fun <T> invoke(failFast: Boolean = true, init: Validation<T>.() -> Unit): Validation<T> {
            val validation = Validation<T>(failFast = failFast)
            return validation.apply(init)
        }
    }
}

internal typealias DynamicValidation<T> = Validation<T>.(T) -> Unit

fun List<ValidationError>.eagerErrors(): List<ValidationError> {
    return this.groupBy { it.propertyPath }
        .map { it.value.first() }
}
