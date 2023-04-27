package io.validk.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest

@MicronautTest
class RestReturnAsCustomField(embeddedServer: EmbeddedServer) : TestBase(embeddedServer) {

    init {
        "test" {
            val data = mapOf("name" to "john")
            postJson("/rest/update-name/return-as-custom-field", data) shouldRespondWithJson "/response/rest/validbody-customfield.json"
        }
    }
}

@Controller
class RestReturnAsCustomFieldController {
    @Post("/rest/update-name/return-as-custom-field")
    @ValidBody(returnBody = ReturnBody.FIELD, returnField = "submittedData")
    fun updateName(@Body form: TestRequest) = HttpResponse.ok(mapOf("success" to true))
}
