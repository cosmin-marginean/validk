package com.test.controller.rest

import com.test.controller.CreateUserForm
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.validk.micronaut.ValidBody

@Controller
class RestDefault {

    @Post("/users/create")
    @ValidBody
    fun createUser(@Body form: CreateUserForm): HttpResponse<*> {
        return HttpResponse.ok(mapOf("success" to true))
    }
}
