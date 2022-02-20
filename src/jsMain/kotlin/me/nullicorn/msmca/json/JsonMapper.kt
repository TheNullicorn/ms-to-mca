package me.nullicorn.msmca.json

import kotlin.js.Json

internal actual object JsonMapper {

    actual fun parseObject(jsonString: String): JsonObjectView =
        JsJsonObjectView(jsonString.toJson<Any>().unsafeCast<Json>())

    actual fun parseArray(jsonString: String): JsonArrayView =
        JsJsonArrayView(jsonString.toJson())

    actual fun stringify(input: Any?): String = try {
        JSON.stringify(input)
    } catch (e: Throwable) {
        throw JsonMappingException("Failed to stringify using JSON.stringify", e)
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
private inline fun <reified T : Any> String.toJson(): T {
    val parsed = try {
        JSON.parse<T>(this)
    } catch (e: Throwable) {
        throw JsonMappingException("Failed to parse using JSON.parse", e)
    }

    return parsed
}