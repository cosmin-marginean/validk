package io.validk.micronaut

import io.kotest.core.spec.style.StringSpec
import io.micronaut.runtime.server.EmbeddedServer
import okhttp3.Response

abstract class TestBase(private val embeddedServer: EmbeddedServer) : StringSpec() {

    private val httpClient = newHttpClient()

    fun postJson(url: String, data: Map<String, String>): Response {
        val response = httpClient.postJson(embeddedServer.relativeUrl(url), data)
        println("Response code for ${url} is ${response.code}")
        return response
    }

    fun postForm(url: String, data: Map<String, String>): Response {
        val response = httpClient.postForm(embeddedServer.relativeUrl(url), data)
        println("Response code for $url is ${response.code}")
        return response
    }
}
