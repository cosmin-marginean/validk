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
class ViewsDefault(embeddedServer: EmbeddedServer) : TestBase(embeddedServer) {

    init {
        "test" {
            postForm("/update-name", mapOf("name" to "john")) shouldRespondWith """
            <form action="/update" method="POST">
                <input type="text" name="name" value="john"/>
                <button type="submit">Submit</button>
                <div class="errors">
                    <div>Name should be at least 5 characters long</div>
                </div>
            </form>"""
        }
    }
}


@Controller
class ViewsDefaultController {
    @Post("/update-name")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @ValidBody(errorView = "update-name")
    fun updateName(@Body form: TestRequest): HttpResponse<Any> = HttpResponse.ok(ModelAndView("update-ok", null))
}

