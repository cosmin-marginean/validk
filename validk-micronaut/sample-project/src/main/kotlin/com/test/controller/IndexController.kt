package com.test.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.views.ModelAndView

@Controller
class IndexController {

    @Get("/")
    fun index() = HttpResponse.ok(ModelAndView("index", null))
}