package me.nullicorn.msmca.json

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonParser

internal actual object JsonMapper {
    /**
     * The local instance of [Gson][com.google.gson.Gson] used for converting objects to JSON strings.
     */
    private val gson = GsonBuilder().disableHtmlEscaping().create()

    actual fun parseObject(jsonString: String): JsonObjectView =
        GsonJsonObjectView(jsonString.toJson())

    actual fun parseArray(jsonString: String): JsonArrayView =
        GsonJsonArrayView(jsonString.toJson())

    actual fun stringify(input: Any?): String = try {
        gson.toJson(input)
    } catch (e: JsonParseException) {
        throw JsonMappingException("Failed to stringify using Gson.toJson", e)
    }
}

/**
 * Internal helper. Attempts to parse the string, using Gson, as a JSON value of type [T].
 *
 * @param[T] The Gson type that the output is expected to be.
 *
 * @throws[JsonMappingException] if the string does not represent valid JSON.
 * @throws[JsonMappingException] if the string, when parsed as JSON, is a different type than [T].
 */
private inline fun <reified T : JsonElement> String.toJson(): T {
    val parsed = try {
        JsonParser.parseString(this)
    } catch (e: JsonParseException) {
        throw JsonMappingException("Failed to parse using JsonParser.parseString", e)
    }

    if (parsed !is T) {
        throw JsonMappingException("Expected JSON to be ${T::class}, but got ${parsed.javaClass}")
    }

    return parsed
}