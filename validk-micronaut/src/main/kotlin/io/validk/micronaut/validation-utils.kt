package io.validk.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.views.ModelAndView
import io.micronaut.views.ViewUtils
import io.validk.ValidObject
import io.validk.ValidationErrors

fun <F> ValidObject<F>.inject(appContext: ApplicationContext): ValidObject<F> {
    appContext.inject(this)
    return this
}

fun ValidObject<*>.validateRequest(onErrors: (ValidationErrors) -> HttpResponse<*>, onSuccess: () -> HttpResponse<*>): HttpResponse<*> {
    val result = this.validate()
    return if (result == null) {
        onSuccess()
    } else {
        onErrors(result)
    }
}

fun ValidationErrors.badRequest(
    view: String,
    body: Any,
    returnBody: ReturnBody = ReturnBody.ROOT,
    returnField: String? = null
): MutableHttpResponse<*> {
    return HttpResponse.badRequest(ModelAndView(view, responseModel(body, returnBody, returnField)))
}

fun ValidationErrors.badRequest(
    body: Any,
    returnBody: ReturnBody = ReturnBody.ROOT,
    returnField: String? = null
): MutableHttpResponse<*> {
    return HttpResponse.badRequest(responseModel(body, returnBody, returnField))
}

fun ValidationErrors.responseModel(
    body: Any,
    returnBody: ReturnBody = ReturnBody.ROOT,
    returnField: String? = null
): Map<String, Any> {
    val responseModel = mutableMapOf<String, Any>()
    responseModel["errors"] = this

    when (returnBody) {
        ReturnBody.ROOT -> responseModel.putAll(ViewUtils.modelOf(body))
        ReturnBody.FIELD -> {
            if (returnField.isNullOrEmpty()) {
                throw IllegalStateException("${ReturnBody.FIELD} requires a returnField")
            }
            responseModel[returnField] = body
        }

        else -> {}
    }

    return responseModel
}
