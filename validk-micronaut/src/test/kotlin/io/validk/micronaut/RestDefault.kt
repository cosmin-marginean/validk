package io.validk.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest

@MicronautTest
class RestDefault(embeddedServer: EmbeddedServer) : TestBase(embeddedServer) {

    init {
        "test" {
            postJson("/rest/update-name", mapOf("name" to "john")) shouldRespondWithJson "/response/rest/validbody-default.json"
        }
    }

}

@Controller
class RestDefaultController {
    @Post("/rest/update-name")
    @ValidBody
    fun updateName(@Body form: TestRequest, request: HttpRequest<*>): HttpResponse<Any> = HttpResponse.ok(mapOf("success" to true))
}

