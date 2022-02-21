package me.nullicorn.msmca

/**
 * Indicates that some form of authentication failed.
 *
 * Causes could include connection issues, user errors, and server errors.
 */
open class AuthException(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause)

/*
 * Until KT-43490 is fixed, `message` and `cause` have to be regular parameters passed into
 * Exception's constructor (as opposed to using `override val` for each).
 *
 * See https://youtrack.jetbrains.com/issue/KT-43490
 */