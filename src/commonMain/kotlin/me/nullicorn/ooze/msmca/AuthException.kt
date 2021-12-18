package me.nullicorn.ooze.msmca

/**
 * Indicates that some form of authentication failed.
 *
 * Causes could include connection issues, user errors, and server errors.
 */
open class AuthException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : Exception()