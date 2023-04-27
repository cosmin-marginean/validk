package com.test.controller.rest

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.validk.ValidationErrors
import io.validk.micronaut.OnValidationErrors
import io.validk.micronaut.ValidBody

@Controller
class RestCustomHandler {

    @Post("/users/{userId}/update")
    @ValidBody
    fun updateUser(
        @PathVariable("userId") userId: String,
        @Body form: UpdateUserForm
    ): HttpResponse<*> {
        return HttpResponse.ok(mapOf("success" to true))
    }

    @OnValidationErrors("/users/{userId}/update")
    fun updateUserErrors(
        @PathVariable("userId") userId: String,
        errors: ValidationErrors,
        request: UpdateUserForm
    ): HttpResponse<*> {
        return HttpResponse.badRequest(mapOf("message" to "This won't fly"))
    }
}
