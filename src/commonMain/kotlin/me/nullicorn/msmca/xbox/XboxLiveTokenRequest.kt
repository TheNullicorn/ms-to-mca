package me.nullicorn.msmca.xbox

import me.nullicorn.msmca.http.Request

/**
 * Internal request used to authenticate & generate an Xbox Live API token.
 *
 * @param[context] The Xbox Live API to use, such as `user` or `xsts`.
 * @param[endpoint] The API endpoint to send the request to.
 * @param[relyingParty] The URI of the third party that the token is intended for.
 * @param[properties] Arbitrary data to include in the request's JSON body.
 */
internal data class XboxLiveTokenRequest(
    val context: String,
    val endpoint: String,
    val relyingParty: String,
    val properties: Map<String, Any?>,
) : Request {
    override val url: String
        get() = "https://$context.auth.xboxlive.com/$context/$endpoint"

    override val method: String
        get() = "POST"

    override val headers: Map<String, String>
        get() = mapOf(
            "accept" to "application/json",
            "content-type" to "application/json",
        )

    override val body: Map<String, Any?>
        get() = mapOf(
            "TokenType" to "JWT",
            "Properties" to properties,
            "RelyingParty" to relyingParty,
        )

    companion object {
        /**
         * Creates a request for an Xbox Live user token.
         *
         * @param[microsoftToken] A general-purpose Microsoft access token, received from OAuth or OpenID authentication.
         */
        @Suppress("HttpUrlsUsage")
        fun user(microsoftToken: String) = XboxLiveTokenRequest(
            context = "user",
            endpoint = "authenticate",
            relyingParty = "http://auth.xboxlive.com",
            properties = mapOf(
                "SiteName" to "user.auth.xboxlive.com",
                "RpsTicket" to "d=$microsoftToken",
                "AuthMethod" to "RPS",
            ),
        )

        fun xsts(userToken: String) = XboxLiveTokenRequest(
            context = "xsts",
            endpoint = "authorize",
            relyingParty = "rp://api.minecraftservices.com/",
            properties = mapOf(
                "SandboxId" to "RETAIL",
                "UserTokens" to Array(size = 1) { userToken },
            ),
        )
    }
}