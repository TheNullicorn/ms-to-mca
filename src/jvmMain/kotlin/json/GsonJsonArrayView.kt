package me.nullicorn.msmca.json

import com.google.gson.JsonArray

/**
 * An unmodifiable view of a Gson [JsonArray].
 */
internal class GsonJsonArrayView(private val actual: JsonArray) : JsonArrayView {
    override val length: Int
        get() = actual.size()

    override fun get(index: Int): Any? {
        if (index in 0 until actual.size())
            return actual[index]?.jsonView

        return null
    }

    override fun toString() = actual.toString()
}