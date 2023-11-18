package io.validk.micronaut.internals

import io.micronaut.aop.MethodInvocationContext
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.PathVariable
import io.validk.ValidObject
import io.validk.ValidationErrors
import io.validk.micronaut.OnValidationErrors
import kotlin.reflect.KFunction
import kotlin.reflect.full.*

internal class CustomErrorHandler(
    private val context: MethodInvocationContext<Any, Any>,
    private val errors: ValidationErrors,
    private val body: ValidObject<*>
) {

    private val customErrorHandler: KFunction<*>? = context.target::class
        .functions
        .firstOrNull {
            val errorAnnotation = it.findAnnotation<OnValidationErrors>()
            errorAnnotation != null && errorAnnotation.path == context.uriTemplate()
        }

    val present: Boolean = customErrorHandler != null

    fun execute(): HttpResponse<*> {
        if (customErrorHandler == null) {
            throw IllegalStateException("No custom error handler found for ${context.asString()}")
        }

        val pathParams = context.parameters.values.filter { it.isAnnotationPresent(PathVariable::class.java) }

        val args = mutableListOf<Any>(context.target)
        // Inject custom handler arguments
        customErrorHandler.valueParameters.forEach { handlerParam ->
            when {
                // Inject path variables
                handlerParam.hasAnnotation<PathVariable>() -> {
                    val variableName = handlerParam.findAnnotation<PathVariable>()!!.value
                    val pathParam = pathParams.find { param ->
                        param.getAnnotation(PathVariable::class.java).get("value", String::class.java).get() == variableName
                    }
                    args.add(pathParam!!.value)
                }

                // Inject ValidationErrors
                handlerParam.type.isSubtypeOf(ValidationErrors::class.starProjectedType) -> args.add(errors)

                // Inject the submitted Form/Request
                handlerParam.type.isSubtypeOf(this.body::class.starProjectedType) -> args.add(this.body)
                else -> throw IllegalStateException("Cannot handle parameter ${handlerParam.name} of type ${handlerParam.type}")
            }
        }

        return customErrorHandler.call(*args.toTypedArray()) as HttpResponse<*>
    }
}
