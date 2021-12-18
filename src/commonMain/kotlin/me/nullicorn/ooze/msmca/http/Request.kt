package me.nullicorn.ooze.msmca.http

/**
 * Information sent to an HTTP server, typically expecting a [response][Response].
 */
interface Request {
    /**
     * The host and path that the request should be sent to.
     *
     * Format: ```[scheme]://[host][:port]/[path]```
     */
    val url: String

    /**
     * The HTTP method that should be used to send the request.
     *
     * e.g. `GET`, `POST`, `PUT`, etc.
     */
    val method: String

    /**
     * The HTTP [Headers] that should be sent before the request's [body].
     */
    val headers: Headers
        get() = emptyMap()

    /**
     * Any extra information to include in the request. (Optional)
     */
    val body: Map<String, Any?>?
        get() = null
}