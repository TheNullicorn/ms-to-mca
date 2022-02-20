package me.nullicorn.msmca.xbox

import me.nullicorn.msmca.http.Response
import me.nullicorn.msmca.json.JsonMappingException

/**
 * Known error codes that can be received from Xbox Live APIs.
 *
 * The enum provides abstraction over the raw error codes, especially for adding custom error
 * messages and i18n.
 */
enum class XboxLiveError {
    /**
     * Indicates that Xbox Live sent an unrecognized error code.
     */
    UNKNOWN,

    // User errors.

    /**
     * Indicates that the user's Microsoft account has no corresponding Xbox Live account.
     */
    XBOX_NOT_LINKED,

    /**
     * Indicates that the account is a child, and must be added to a Microsoft family to
     * authenticate.
     */
    AGE_TOO_YOUNG,

    /**
     * Indicates that the user needs to verify their age at xbox.com to authenticate.
     */
    AGE_NOT_VERIFIED,

    /**
     * Indicates that Xbox Live is banned in the user's country or region.
     */
    REGION_NOT_ALLOWED,

    // Technical errors.

    /**
     * Indicates that Xbox Live's authentication service is experiencing an outage.
     */
    OUTAGE,

    /**
     * Indicates that the client was denied access to Xbox Live's production environment. This
     * should not happen.
     */
    SANDBOX_NOT_ALLOWED,

    /**
     * Indicates that an invalid Microsoft access token was used to authenticate.
     *
     * A new one can be requested using the Microsoft Authentication Library (MSAL).
     */
    MICROSOFT_TOKEN_INVALID,

    /**
     * Indicates that an expired Microsoft access token was used to authenticate.
     *
     * A new one can be requested using the Microsoft Authentication Library (MSAL).
     */
    MICROSOFT_TOKEN_EXPIRED,

    /**
     * Indicates that an expired user token was used to authenticate.
     *
     * A new one can be requested using
     * [XboxLiveAuth.getUserToken][XboxLiveAuth.getUserToken].
     */
    USER_TOKEN_EXPIRED,

    /**
     * Indicates that an invalid user token was used to authenticate.
     *
     * A valid one can be requested using
     * [XboxLiveAuth.getUserToken][XboxLiveAuth.getUserToken].
     */
    USER_TOKEN_INVALID,

    /**
     * Indicates that an expired service token was used to authenticate.
     *
     * A new one can be requested using
     * [XboxLiveAuth.getServiceToken][XboxLiveAuth.getServiceToken].
     */
    SERVICE_TOKEN_EXPIRED,

    /**
     * Indicates that an invalid service token was used to authenticate.
     *
     * A valid one can be requested using
     * [XboxLiveAuth.getServiceToken][XboxLiveAuth.getServiceToken].
     */
    SERVICE_TOKEN_INVALID;

    internal companion object {
        /**
         * Converts an Xbox Live error code to one of the known enum values.
         *
         * @param[xErr] the value of the `XErr` field from an Xbox Live API response.
         * @return the corresponding enum value for the error, or [UNKNOWN] if the error code cannot
         * be interpreted.
         */
        internal fun fromXErr(xErr: Any?): XboxLiveError =
            if (xErr is Number) when (xErr.toLong()) {
                // User errors.
                0x8015DC09 -> XBOX_NOT_LINKED
                0x8015DC0B -> REGION_NOT_ALLOWED
                0x8015DC0E -> AGE_TOO_YOUNG
                0x8015DC0C, 0x8015DC0D -> AGE_NOT_VERIFIED

                // Technical errors.
                0x8015DC12 -> SANDBOX_NOT_ALLOWED
                0x8015DC22 -> USER_TOKEN_EXPIRED
                0x8015DC26 -> USER_TOKEN_INVALID
                0x8015DC1F -> SERVICE_TOKEN_EXPIRED
                0x8015DC27 -> SERVICE_TOKEN_INVALID
                0x8015DC31, 0x8015DC32 -> OUTAGE

                // Fallback error.
                else -> UNKNOWN
            } else UNKNOWN
    }
}

/**
 * Interprets the response as an Xbox Live error, if possible.
 *
 * If the response doesn't represent an error, `null` is returned.
 */
internal val Response.xboxLiveError: XboxLiveError?
    get() {
        if (isSuccess) return null

        // XErr can either be a header or body field.
        val xErr = headers["XErr"] ?: try {
            this.asJsonObject()["XErr"]
        } catch (cause: JsonMappingException) {
            null
        }

        return when {
            // Xbox returned a readable error message.
            xErr != null -> XboxLiveError.fromXErr(xErr)
            // 400 indicates that the token is malformed.
            status == 400 -> XboxLiveError.MICROSOFT_TOKEN_INVALID
            // 401 indicates that the token is valid, but expired.
            status == 401 -> XboxLiveError.MICROSOFT_TOKEN_EXPIRED
            // Fall-back reason.
            else -> XboxLiveError.UNKNOWN
        }
    }