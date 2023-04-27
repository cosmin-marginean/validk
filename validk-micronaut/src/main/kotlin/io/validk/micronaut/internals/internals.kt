package io.validk.micronaut.internals

import io.micronaut.aop.MethodInvocationContext
import io.micronaut.http.HttpAttributes
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.HttpMethodMapping
import io.micronaut.web.router.RouteMatch

internal val HttpRequest<*>.routeVariables: Map<String, Any>
    get() {
        return this.getAttribute(HttpAttributes.ROUTE_INFO, RouteMatch::class.java)
            .get()
            .variableValues
    }

internal inline fun <reified T : Annotation, reified R> MethodInvocationContext<*, *>.paramAnnotatedWith(): R {
    return this
        .parameters
        .values
        .first { it.isAnnotationPresent(T::class.java) }
        .value as R
}


internal fun MethodInvocationContext<*, *>.uriTemplate(): String {
    return annotationMetadata.getAnnotationValuesByStereotype<HttpMethodMapping>("io.micronaut.http.annotation.HttpMethodMapping")
        .first()
        .get("value", String::class.java)
        .get()
}

internal fun MethodInvocationContext<*, *>.asString(): String {
    val className = this.targetMethod.declaringClass.name
    return "$className.${this.methodName}"
}
