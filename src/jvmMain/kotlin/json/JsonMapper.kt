package me.nullicorn.msmca.json

import com.github.cliftonlabs.json_simple.JsonException
import com.github.cliftonlabs.json_simple.Jsoner

internal actual object JsonMapper {

    actual fun parseObject(jsonString: String): JsonObjectView =
        SimpleJsonObjectView(jsonString.toJson())

    actual fun parseArray(jsonString: String): JsonArrayView =
        SimpleJsonArrayView(jsonString.toJson())

    actual fun stringify(input: Any?): String = try {
        Jsoner.serialize(input)
    } catch (cause: IllegalArgumentException) {
        throw JsonMappingException("Cannot serialize ${input?.javaClass} as JSON")
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
private inline fun <reified T> String.toJson(): T {
    val parsed = try {
        Jsoner.deserialize(this)
    } catch (cause: JsonException) {
        throw JsonMappingException("Failed to parse using JsonParser.parseString", cause)
    }

    if (parsed !is T) throw JsonMappingException(
        "Expected JSON to be ${T::class.simpleName}, but got ${parsed.javaClass.simpleName}"
    )

    return parsed
}