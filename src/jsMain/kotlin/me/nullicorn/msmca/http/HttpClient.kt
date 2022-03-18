package me.nullicorn.msmca.http

import me.nullicorn.msmca.interop.JsHeaders
import me.nullicorn.msmca.interop.JsResponse
import me.nullicorn.msmca.interop.fetch
import me.nullicorn.msmca.interop.toKtIterator
import me.nullicorn.msmca.json.JsonMapper
import me.nullicorn.msmca.json.toJson
import kotlin.js.json

internal actual object BuiltInHttpClient : HttpClient {

    actual override fun send(request: Request): Response {
        val options = json(
            "method" to request.method,
            "body" to JsonMapper.stringify(request.body?.toJson()),
            "headers" to json(*request.headers.toList().toTypedArray())
        )

        return fetch(request.url, options).toKtResponse()
    }

    private fun JsHeaders.toKtHeaders() = buildMap {
        val jsHeaders = this@toKtHeaders

        for (key in keys().toKtIterator()) {
            if (key == null) continue
            set(key, jsHeaders.get(key))
        }
    }

    private fun JsResponse.toKtResponse() =
        Response(status, headers.toKtHeaders(), text())
}