package me.nullicorn.msmca.minecraft

import me.nullicorn.msmca.AuthException

/**
 * Thrown when Minecraft's authentication service returns a valid response, but one that indicates
 * an error.
 *
 * @param[type] The value of the `errorType` field returned by the service, if present. This is
 * typically *not* a user-friendly message, but explains to the developer what went wrong.
 */
class MinecraftAuthException(
    val type: String?,
    cause: Throwable? = null,
) : AuthException("Xbox Live returned an error: $type", cause)