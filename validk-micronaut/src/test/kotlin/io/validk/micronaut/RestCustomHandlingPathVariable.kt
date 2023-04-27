package io.validk.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest

@MicronautTest
class RestCustomHandlingPathVariable(embeddedServer: EmbeddedServer) : TestBase(embeddedServer) {

    init {
        "test" {
            val data = mapOf("name" to "john")
            postJson(
                "/rest/update-name/custom-handling/300",
                data
            ) shouldRespondWithJson "/response/rest/validbody-customhandling-pathvar.json"
        }
    }
}

@Controller
class RestCustomHandlingPathVariableController {
    @Post("/rest/update-name/custom-handling/{id}")
    @ValidBody
    fun updateName(@Body form: TestRequest, @PathVariable("id") id: String) = HttpResponse.ok(mapOf("success" to true))

    @OnValidationErrors("/rest/update-name/custom-handling/{id}")
    fun onErrors(@PathVariable("id") id: String): HttpResponse<*> {
        return HttpResponse.badRequest(mapOf("message" to "Invalid data for ${id}"))
    }
}
