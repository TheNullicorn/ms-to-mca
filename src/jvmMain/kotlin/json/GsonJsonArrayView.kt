package me.nullicorn.msmca.json

import com.google.gson.JsonArray

/**
 * An unmodifiable view of a Gson [JsonArray].
 */
internal class GsonJsonArrayView(private val actual: JsonArray) : JsonArrayView {
    override val length: Int
        get() = actual.size()

    override fun get(index: Int) = actual[index]?.jsonView

    override fun toString() = actual.toString()
}