package me.nullicorn.msmca.xbox

import me.nullicorn.msmca.http.Response
import me.nullicorn.msmca.json.JsonMappingException

/**
 * Known error codes that can be returned from Xbox Live services.
 *
 * If one is received, it's recommended to display it to users in a friendly format to help them
 * troubleshoot the issue. It may often be the result of user error, or possibly a server issue that
 * is out of the user's control.
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
        internal val errorsByCode: Map<Long?, XboxLiveError> = mapOf(
            null to UNKNOWN,

            // User errors.
            0x8015DC09 to XBOX_NOT_LINKED,
            0x8015DC0B to REGION_NOT_ALLOWED,
            0x8015DC0E to AGE_TOO_YOUNG,
            0x8015DC0C to AGE_NOT_VERIFIED,
            0x8015DC0D to AGE_NOT_VERIFIED,

            // Technical errors.
            0x8015DC12 to SANDBOX_NOT_ALLOWED,
            0x8015DC22 to USER_TOKEN_EXPIRED,
            0x8015DC26 to USER_TOKEN_INVALID,
            0x8015DC1F to SERVICE_TOKEN_EXPIRED,
            0x8015DC27 to SERVICE_TOKEN_INVALID,
            0x8015DC31 to OUTAGE,
            0x8015DC32 to OUTAGE,
        )

        /**
         * Converts an Xbox Live error code to one of the known enum values.
         *
         * @param[xErr] the value of the `XErr` field from an Xbox Live API response.
         * @return the corresponding enum value for the error, or [UNKNOWN] if the error code cannot
         * be interpreted.
         */
        internal fun fromXErr(xErr: Any?): XboxLiveError {
            val numericCode: Long? = when (xErr) {
                // Return numeric codes as-is.
                is Number -> xErr.toLong()

                // Parse string error codes as numbers.
                is String -> try {
                    xErr.toLong()
                } catch (cause: NumberFormatException) {
                    null
                }

                // Anything else should be considered invalid, and thus null.
                else -> null
            }

            return errorsByCode[numericCode] ?: UNKNOWN
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
                    xErr != null -> fromXErr(xErr)
                    // 400 indicates that the token is malformed.
                    status == 400 -> MICROSOFT_TOKEN_INVALID
                    // 401 indicates that the token is valid, but expired.
                    status == 401 -> MICROSOFT_TOKEN_EXPIRED
                    // Fall-back reason.
                    else -> UNKNOWN
                }
            }
    }
}