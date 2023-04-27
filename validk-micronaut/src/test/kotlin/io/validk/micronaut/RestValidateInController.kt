package io.validk.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest

@MicronautTest
class RestValidateInController(embeddedServer: EmbeddedServer) : TestBase(embeddedServer) {

    init {
        "test" {
            val data = mapOf("name" to "john")
            postJson("/rest/update-name/validate-in-controller", data) shouldRespondWithJson "/response/rest/validate-in-controller.json"
        }
    }

}

@Controller
class RestValidateInMethodController {
    @Post("/rest/update-name/validate-in-controller")
    fun updateName(@Body form: TestRequest): HttpResponse<*> {
        return form.validateRequest(
            onErrors = { HttpResponse.badRequest(mapOf("message" to "Data is invalid")) },
            onSuccess = { HttpResponse.badRequest(mapOf("message" to "Invalid data")) }
        )
    }
}
