package io.validk.micronaut

import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.validk.ValidObject
import io.validk.micronaut.internals.CustomErrorHandler
import io.validk.micronaut.internals.paramAnnotatedWith
import jakarta.inject.Singleton
import kotlin.jvm.optionals.getOrElse

@Singleton
@InterceptorBean(ValidBody::class)
class ValidBodyInterceptor(private val appContext: ApplicationContext) : MethodInterceptor<Any, Any> {

    override fun intercept(context: MethodInvocationContext<Any, Any>): Any {
        val body = context.paramAnnotatedWith<Body, ValidObject<*>>()
        return body
            .inject(appContext)
            .validateRequest(
                onSuccess = { context.proceed() as HttpResponse<*> },
                onErrors = { errors ->
                    val customHandler = CustomErrorHandler(context, errors, body)
                    if (customHandler.present) {
                        customHandler.execute()
                    } else {
                        val validBody = context.getAnnotation(ValidBody::class.java)!!
                        val returnBody = validBody.get("returnBody", ReturnBody::class.java).getOrElse { ReturnBody.ROOT }
                        val returnField = validBody.get("returnField", String::class.java).getOrElse { null }
                        val errorView = validBody.get("errorView", String::class.java).getOrElse { null }

                        if (!errorView.isNullOrEmpty()) {
                            // We need to return a view here
                            errors.badRequest(errorView, body, returnBody, returnField)
                        } else {
                            // We simply return the model
                            errors.badRequest(body, returnBody, returnField)
                        }
                    }
                }
            )
    }
}

