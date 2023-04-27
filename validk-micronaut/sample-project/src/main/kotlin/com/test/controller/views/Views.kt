package com.test.controller.views

import com.test.controller.CreateUserForm
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.views.ModelAndView
import io.validk.micronaut.ValidBody

@Controller
class Views {

    @Get("/users/create")
    fun view() = HttpResponse.ok(ModelAndView("create-user", null))

    @Post("/users/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ValidBody(errorView = "create-user")
    fun createUser(@Body form: CreateUserForm): HttpResponse<*> {
        return HttpResponse.ok(ModelAndView("user-created", null))
    }
}
