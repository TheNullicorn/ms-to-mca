package me.nullicorn.msmca.json

import com.github.cliftonlabs.json_simple.JsonObject

/**
 * An unmodifiable view of a json-simple [JsonObject].
 */
internal class SimpleJsonObjectView(private val actual: JsonObject) : JsonObjectView {

    override fun get(key: String) = actual[key]?.jsonView

    override fun toString() = actual.toString()
}