package me.nullicorn.msmca.xbox

import me.nullicorn.msmca.AuthException

/**
 * Thrown when an Xbox Live service returns a valid response, but one that indicates an error.
 *
 * It's recommended to display the [reason] to users in a friendly format to help them troubleshoot
 * the issue. It may often be due to user error, or a server issue out of the user's control.
 *
 * @param[reason] The error returned by the service.
 */
class XboxLiveAuthException(val reason: XboxLiveError = XboxLiveError.UNKNOWN) :
    AuthException("Xbox Live returned an error: $reason")