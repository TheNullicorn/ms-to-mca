package me.nullicorn.msmca.json

import kotlin.js.Json
import kotlin.js.json

/**
 * Converts a Kotlin map into a generic JavaScript object, whose keys and values come from the
 * original map.
 *
 * This operation is recursive, converting any sub-maps into JavaScript objects, as well as any
 * [Iterable]s into JavaScript arrays.
 */
internal fun Map<*, *>.toJson(): Json {
    val result = json()

    for ((key, value) in this) {
        if (key == null) continue

        val safeKey = key.toString()
        val safeValue = when (value) {
            is Map<*, *> -> value.toJson()
            is Iterable<*> -> value.toJson()
            else -> value
        }

        result[safeKey] = safeValue
    }

    return result
}

/**
 * Behaves the same as [Map.toJson], but operates on an [Iterable] instead of a [Map]
 */
private fun Iterable<*>.toJson(): Array<*> {
    val result = mutableListOf<dynamic>()

    for (element in this) {
        val safeElement = when (element) {
            is Map<*, *> -> element.toJson()
            is Iterable<*> -> element.toJson()
            else -> element
        }

        result.add(safeElement)
    }

    return result.toTypedArray()
}