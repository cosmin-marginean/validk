package com.test.controller.views

import com.test.controller.CreateUserForm
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.views.ModelAndView
import io.validk.ValidationErrors
import io.validk.micronaut.*

@Controller
class ViewsCustomHandler {

    @Get("/users/create/custom-handler")
    fun view() = HttpResponse.ok(ModelAndView("create-user-custom-handler", null))

    @Post("/users/create/custom-handler")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ValidBody
    fun createUser(@Body form: CreateUserForm): HttpResponse<*> {
        return HttpResponse.ok(ModelAndView("user-created", null))
    }

    @OnValidationErrors("/users/create/custom-handler")
    fun onCreateUserErrors(errors: ValidationErrors, form: CreateUserForm): HttpResponse<*> {
        println("Caught some errors: ${errors.errorMessages}")
        return errors.badRequest("create-user-custom-handler", form)
    }
}

