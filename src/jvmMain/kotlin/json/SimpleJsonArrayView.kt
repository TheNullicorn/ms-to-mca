package me.nullicorn.msmca.json

import com.github.cliftonlabs.json_simple.JsonArray

/**
 * An unmodifiable view of a json-simple [JsonArray].
 */
internal class SimpleJsonArrayView(private val actual: JsonArray) : JsonArrayView {
    override val length: Int
        get() = actual.size

    override fun get(index: Int): Any? {
        if (index in 0 until actual.size)
            return actual[index]?.jsonView

        return null
    }

    override fun toString() = actual.toString()
}