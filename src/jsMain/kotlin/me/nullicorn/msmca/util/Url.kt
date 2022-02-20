package me.nullicorn.msmca.util

import org.w3c.dom.url.URL

internal actual val String.isUrl: Boolean
    get() = try {
        URL(this)
        // If nothing was thrown, it's a good URL.
        true
    } catch (cause: Throwable) {
        false
    }