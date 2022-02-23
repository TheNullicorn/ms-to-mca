package me.nullicorn.msmca.http

/**
 * A simple HTTP client used to access online services for Xbox Live & Minecraft.
 */
interface HttpClient {
    /**
     * Synchronously sends an HTTP [request] to the server, and awaits the [response][Response].
     *
     * @param[request] The HTTP method, URL, headers, and (optional) body to send to the server.
     * @return The HTTP status, headers, and body that the server sent in response.
     *
     * @throws[HttpException] if the connection fails, or if the server sends a malformed response.
     */
    fun send(request: Request): Response
}

internal expect object BuiltInHttpClient : HttpClient {
    actual override fun send(request: Request): Response
}