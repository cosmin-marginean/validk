package com.test.controller.views

import com.test.controller.CreateUserForm
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.views.ModelAndView
import io.validk.micronaut.badRequest
import io.validk.micronaut.validateRequest

@Controller
class ViewsManualValidation {

    @Get("/users/create/manual-validation")
    fun view() = HttpResponse.ok(ModelAndView("create-user-manual-validation", null))

    @Post("/users/create/manual-validation")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun createUser(@Body form: CreateUserForm): HttpResponse<*> {
        return form.validateRequest(
            onErrors = { it.badRequest("create-user-manual-validation", form) },
            onSuccess = { HttpResponse.ok(ModelAndView("user-created", null)) }
        )
    }
}

