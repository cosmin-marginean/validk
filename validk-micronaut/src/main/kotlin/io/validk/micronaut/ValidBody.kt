package io.validk.micronaut

import io.micronaut.aop.Around

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@Around
annotation class ValidBody(
    val returnBody: ReturnBody = ReturnBody.ROOT,
    val returnField: String = "",
    val errorView: String = ""
)

enum class ReturnBody {
    ROOT,
    DONT_RETURN,
    FIELD
}