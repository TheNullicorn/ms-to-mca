package me.nullicorn.msmca.http

/**
 * A minimal HTTP client.
 */
interface HttpClient {
    /**
     * Attempts to send an HTTP [request] to the server, and awaits the [response][Response].
     *
     * @param[request] The HTTP method, URL, headers, and (optional) body to send to the server.
     * @return The HTTP headers and body that the server sends in response.
     * @throws[HttpException] if the connection fails, or if the server sends a malformed response.
     */
    fun send(request: me.nullicorn.msmca.http.Request): me.nullicorn.msmca.http.Response
}

internal expect object BuiltInHttpClient : me.nullicorn.msmca.http.HttpClient {
    actual override fun send(request: me.nullicorn.msmca.http.Request): me.nullicorn.msmca.http.Response
}