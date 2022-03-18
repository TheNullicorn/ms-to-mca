package me.nullicorn.msmca.json

import kotlin.js.Json
import kotlin.js.json

/**
 * Converts a Kotlin map into a generic JavaScript object, whose keys and values come from the
 * original map.
 */
internal fun Map<String, Any?>?.toJson(): Json? =
    if (this == null) null
    else json(*this.toList().toTypedArray())