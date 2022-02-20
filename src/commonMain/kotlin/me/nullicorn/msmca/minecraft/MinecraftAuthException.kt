package me.nullicorn.msmca.minecraft

import me.nullicorn.msmca.AuthException

/**
 * Indicates that a Minecraft service returned an error.
 *
 * @param[type] The value of the "`errorType`" field returned by the service, if present. This is
 * typically *not* a user-friendly message.
 */
class MinecraftAuthException(val type: String?, override val cause: Throwable? = null) :
    AuthException("Xbox Live returned an error: $type")