package me.nullicorn.msmca.interop

import kotlin.js.Json

@JsNonModule
@JsModule("sync-fetch")
@JsName("fetch")
internal external fun fetch(url: String, options: Json): JsResponse

internal external class JsResponse {
    val status: Int
    val headers: JsHeaders
    fun text(): String
}

internal external class JsHeaders {
    fun get(name: String): String
    fun keys(): JsIterator<String>
}