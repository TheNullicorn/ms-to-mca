package me.nullicorn.msmca.json

import kotlin.js.Json

internal class JsJsonObjectView(private val actual: Json) : JsonObjectView {

    override fun get(key: String) = actual[key]?.jsonView

    override fun toString() = JSON.stringify(actual)
}