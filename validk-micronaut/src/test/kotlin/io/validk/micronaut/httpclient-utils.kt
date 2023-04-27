package io.validk.micronaut

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import io.micronaut.http.MediaType
import okhttp3.FormBody
import okhttp3.Headers.Companion.toHeaders
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.StringReader
import java.time.Duration
import java.time.temporal.ChronoUnit

private fun httpClientBuilder(followRedirects: Boolean): OkHttpClient.Builder {
    val builder = OkHttpClient().newBuilder()
        .connectTimeout(Duration.of(120, ChronoUnit.SECONDS))
        .readTimeout(Duration.of(120, ChronoUnit.SECONDS))
        .followRedirects(followRedirects)
    return builder
}

fun newHttpClient(followRedirects: Boolean = true): OkHttpClient {
    val builder = httpClientBuilder(followRedirects)
    return builder.build()
}

fun OkHttpClient.get(url: String, headers: Map<String, String> = emptyMap()): Response {
    val request = Request.Builder()
        .url(url)
        .get()
        .headers(headers.toHeaders())
        .build()
    return newCall(request).execute()
}

fun OkHttpClient.post(url: String, mediaType: String, data: String, headers: Map<String, String> = emptyMap()): Response {
    val postHeaders = mutableMapOf<String, String>()
    postHeaders["Content-Type"] = mediaType
    postHeaders.putAll(headers)
    val request = Request.Builder()
        .url(url)
        .post(data.toRequestBody())
        .headers(postHeaders.toHeaders())
        .build()
    return newCall(request).execute()
}


fun OkHttpClient.postForm(url: String, data: Map<String, String>, headers: Map<String, String> = emptyMap()): Response {
    val formBody = FormBody.Builder()
    data.forEach { (key, value) -> formBody.add(key, value) }
    val postHeaders = mutableMapOf<String, String>()
    postHeaders["Content-Type"] = "application/x-www-form-urlencoded"
    postHeaders.putAll(headers)
    val request = Request.Builder()
        .url(url)
        .post(formBody.build())
        .headers(postHeaders.toHeaders())
        .build()
    return newCall(request).execute()
}

fun OkHttpClient.postJson(url: String, data: Any, headers: Map<String, String> = emptyMap()): Response {
    return post(
        url = url,
        mediaType = MediaType.APPLICATION_JSON,
        data = Klaxon().toJsonString(data),
        headers = headers
    )
}

fun Response.text(): String {
    val string = this.body!!.string()
    this.body!!.close()
    return string
}

fun Response.json(): JsonObject {
    return Klaxon().parseJsonObject(StringReader(text()))
}

