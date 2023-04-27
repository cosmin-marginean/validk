package io.validk.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest

@MicronautTest
class RestCustomHandling(embeddedServer: EmbeddedServer) : TestBase(embeddedServer) {

    init {
        "test" {
            val data = mapOf("name" to "john")
            postJson("/rest/update-name/custom-handling", data) shouldRespondWithJson "/response/rest/validbody-customhandling.json"
        }
    }
}

@Controller
class RestCustomHandlingController {
    @Post("/rest/update-name/custom-handling")
    @ValidBody
    fun updateName(@Body form: TestRequest) = HttpResponse.ok(mapOf("success" to true))

    @OnValidationErrors("/rest/update-name/custom-handling")
    fun onErrors(): HttpResponse<*> {
        return HttpResponse.badRequest(mapOf("message" to "Invalid data"))
    }
}
