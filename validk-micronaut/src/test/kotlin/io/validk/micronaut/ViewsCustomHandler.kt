package io.validk.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import io.micronaut.views.ModelAndView

@MicronautTest
class ViewsCustomHandler(embeddedServer: EmbeddedServer) : TestBase(embeddedServer) {

    init {
        "test" {
            postForm("/views/update-name/custom-handling", mapOf("name" to "john")) shouldRespondWith "Sorry, this won't work"
        }
    }
}

@Controller
class ViewsCustomHandlerController {
    @Post("/views/update-name/custom-handling")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ValidBody
    fun updateName(@Body form: TestRequest): HttpResponse<Any> = HttpResponse.ok(ModelAndView("update-ok", null))

    @OnValidationErrors("/views/update-name/custom-handling")
    fun onErrors(form: TestRequest): HttpResponse<*> {
        return HttpResponse.badRequest(ModelAndView("custom-handling", null))
    }
}
