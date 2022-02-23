package me.nullicorn.msmca.json

import com.github.cliftonlabs.json_simple.JsonArray
import com.github.cliftonlabs.json_simple.JsonObject

/**
 * Internal helper. Converts the current Gson object to either a JSON view, or the corresponding
 * built-in type (`null`, [String], [Number], or [Boolean]).
 *
 * @return - a [JsonObjectView] of the value, if it's an [object][JsonObject]
 * - a [JsonArrayView] of the value, if it's an [array][JsonArray]
 * - the value itself, if it's a [Number], [Boolean], [String], or `null`.
 *
 * @throws IllegalArgumentException if the value's runtime type is not JSON compatible.
 *
 * @see[JsonObjectView]
 * @see[JsonArrayView]
 */
internal val Any?.jsonView: Any?
    get() = when (this) {
        is String, is Number, is Boolean, null -> this
        is JsonArray -> SimpleJsonArrayView(this)
        is JsonObject -> SimpleJsonObjectView(this)
        else -> throw IllegalArgumentException("No viewable runtime type for $javaClass")
    }