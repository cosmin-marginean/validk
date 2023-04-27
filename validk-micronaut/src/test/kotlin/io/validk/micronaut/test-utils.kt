package io.validk.micronaut

import com.beust.klaxon.Klaxon
import io.kotest.matchers.resource.resourceAsString
import io.kotest.matchers.shouldBe
import io.micronaut.runtime.server.EmbeddedServer
import okhttp3.Response
import java.io.InputStream
import java.io.StringReader

val klaxon = Klaxon()

fun EmbeddedServer.relativeUrl(relativeUrl: String): String {
    return "http://localhost:" + this.port + relativeUrl
}

val REGEX_WHITESPACE = "\\s+".toRegex()

fun String.cleanWhitespace(): String {
    return replace(REGEX_WHITESPACE, " ").trim()
}

infix fun Response.shouldRespondWithJson(classpathJson: String) {
    this.text() shouldBeSameJsonAs classpathJson
}

infix fun Response.shouldRespondWith(text: String) {
    this.text().cleanWhitespace() shouldBe text.cleanWhitespace()
}

infix fun String.shouldBeSameJsonAs(classpathJson: String) {
    val json1 = klaxon.parseJsonObject(StringReader(this))
    val json2 = klaxon.parseJsonObject(StringReader(resourceAsString(classpathJson)))
    json1.toJsonString(canonical = true) shouldBe json2.toJsonString(canonical = true)
}

fun resourceAsInput(classpathLocation: String): InputStream {
    return Thread.currentThread()
        .contextClassLoader
        .getResourceAsStream(classpathLocation)!!
}
