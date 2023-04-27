package com.test.controller.rest

import com.test.controller.CreateUserForm
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.validk.micronaut.validateRequest

@Controller
class RestManualValidation {

    @Post("/users/create/manual-validation")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    fun createUser(@Body form: CreateUserForm): HttpResponse<*> {
        return form.validateRequest(
            onErrors = { HttpResponse.badRequest(mapOf("message" to "This won't fly")) },
            onSuccess = { HttpResponse.ok(mapOf("success" to true)) }
        )
    }
}

