package com.test.controller.rest

import com.test.controller.CreateUserForm
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.validk.micronaut.ReturnBody
import io.validk.micronaut.ValidBody

@Controller
class RestReturnBodyAsField {

    @Post("/users/create/return-body-as-field")
    @ValidBody(returnBody = ReturnBody.DONT_RETURN)
    fun createUser(@Body form: CreateUserForm): HttpResponse<*> {
        return HttpResponse.ok(mapOf("success" to true))
    }
}
