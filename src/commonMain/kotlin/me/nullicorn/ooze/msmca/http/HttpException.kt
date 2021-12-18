package me.nullicorn.ooze.msmca.http

/**
 * Indicates that an HTTP connection could not start or end for some [reason][message].
 */
internal class HttpException(
    override val message: String?,
    override val cause: Throwable? = null,
) : Exception()