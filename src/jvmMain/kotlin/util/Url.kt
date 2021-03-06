package me.nullicorn.msmca.util

import java.net.MalformedURLException
import java.net.URL

internal actual val String.isUrl: Boolean
    get() = try {
        URL(this)
        // If nothing was thrown, it's a good URL.
        true
    } catch (cause: MalformedURLException) {
        false
    }