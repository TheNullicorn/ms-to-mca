package me.nullicorn.ooze.msmca.json

import com.google.gson.JsonObject

/**
 * An unmodifiable view of a Gson [JsonObject].
 */
internal class GsonJsonObjectView(private val actual: JsonObject) : JsonObjectView {
    override val keys: Iterable<String>
        get() = actual.keySet()

    override fun get(key: String) = actual[key]?.jsonView

    override fun toString() = actual.toString()
}