package me.nullicorn.msmca.json

import kotlin.js.Json

internal val Any?.jsonView: Any?
    get() = when (this) {
        // JSON primitives stay as-is.
        is Number, is Boolean, is String, null -> this
        // Arrays & objects get wrapped in their respective view classes.
        is Array<*> -> JsJsonArrayView(this)
        else -> JsJsonObjectView(this.unsafeCast<Json>())
    }