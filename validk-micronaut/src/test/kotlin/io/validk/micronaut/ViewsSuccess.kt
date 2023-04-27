package io.validk.micronaut

import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest

@MicronautTest
class ViewsSuccess(embeddedServer: EmbeddedServer) : TestBase(embeddedServer) {

    init {
        "test" {
            postForm("/update-name", mapOf("name" to "john smith")) shouldRespondWith "OK"
        }
    }
}
