package io.validk.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest

@MicronautTest
class RestDontReturnBody(embeddedServer: EmbeddedServer) : TestBase(embeddedServer) {

    init {
        "test" {
            val data = mapOf("name" to "john")
            postJson("/rest/update-name/dontreturnbody", data) shouldRespondWithJson "/response/rest/validbody-dontreturnbody.json"
        }
    }
}


@Controller
class RestDontReturnBodyController {
    @Post("/rest/update-name/dontreturnbody")
    @ValidBody(returnBody = ReturnBody.DONT_RETURN)
    fun updateName(@Body form: TestRequest) = HttpResponse.ok(mapOf("success" to true))
}
