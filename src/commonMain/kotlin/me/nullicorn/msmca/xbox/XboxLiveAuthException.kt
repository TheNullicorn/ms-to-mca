package me.nullicorn.msmca.xbox

import me.nullicorn.msmca.AuthException

/**
 * Indicates that an Xbox Live service returned an error code.
 *
 * The [reason] value can be used to present a message to the end user.
 */
class XboxLiveAuthException(val reason: XboxLiveError = XboxLiveError.UNKNOWN) :
    me.nullicorn.msmca.AuthException("Xbox Live returned an error: $reason")