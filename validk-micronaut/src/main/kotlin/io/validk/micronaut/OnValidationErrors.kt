package io.validk.micronaut

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class OnValidationErrors(
    val path: String
)
