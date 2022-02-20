package me.nullicorn.msmca.json

import com.google.gson.*

/**
 * Internal helper. Converts the current Gson object to either a JSON view, or the corresponding
 * built-in type (`null`, [String], [Number], or [Boolean]).
 *
 * @return - a [JsonObjectView] of the value, if it's an [object][JsonObject]
 * - a [JsonArrayView] of the value, if it's an [array][JsonArray]
 * - the value's [Number], [String], or [Boolean] value, if it's a JSON [primitive][JsonPrimitive]
 * of the respective type
 * - `null` if the value is `null` or an instance of [JsonNull][com.google.gson.JsonNull]
 *
 * @throws IllegalArgumentException if the value is of an unknown JSON type.
 *
 * @see[JsonObjectView]
 * @see[JsonArrayView]
 */
internal val JsonElement.jsonView: Any?
    get() = when (this) {
        is JsonNull -> null
        is JsonArray -> GsonJsonArrayView(asJsonArray)
        is JsonObject -> GsonJsonObjectView(asJsonObject)
        is JsonPrimitive ->
            if (isString) asString
            else if (isNumber) asNumber
            else if (isBoolean) asBoolean
            else throw IllegalArgumentException("No boxed type for JSON primitive $javaClass")
        else -> throw IllegalArgumentException("No viewable runtime type for $javaClass")
    }