package me.nullicorn.msmca.http

/**
 * Thrown when an HTTP exception closes unexpectedly, or when the server's response is not a valid
 * HTTP response.
 */
internal class HttpException(
    override val message: String?,
    override val cause: Throwable? = null,
) : Exception()