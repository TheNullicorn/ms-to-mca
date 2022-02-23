package me.nullicorn.msmca.minecraft

import me.nullicorn.msmca.AuthException
import me.nullicorn.msmca.http.BuiltInHttpClient
import me.nullicorn.msmca.http.HttpClient
import me.nullicorn.msmca.http.HttpException
import me.nullicorn.msmca.json.JsonMappingException
import me.nullicorn.msmca.xbox.XboxLiveAuth
import me.nullicorn.msmca.xbox.XboxLiveAuthException
import me.nullicorn.msmca.xbox.XboxLiveToken

/**
 * Provides methods for authenticating with Minecraft-related services.
 *
 * @param[httpClient] An HTTP client used to send requests to Minecraft's authentication service.
 * @param[xboxClient] An Xbox Live authentication client, used by the [loginWithXbox] method.
 */
class MinecraftAuth(private val httpClient: HttpClient, private val xboxClient: XboxLiveAuth) {

    /**
     * Creates a client that communicates with Minecraft's authentication service via the provided
     * [httpClient].
     *
     * If the [loginWithXbox] method is used, requests to Xbox Services will be sent using that
     * [httpClient] as well.
     */
    constructor(httpClient: HttpClient) : this(httpClient, XboxLiveAuth(httpClient))

    /**
     * Creates a client that communicates with Minecraft's authentication service via a builtin
     * [HttpClient].
     */
    constructor() : this(BuiltInHttpClient)

    /**
     * Exchanges an Xbox Live [service token][XboxLiveAuth.getServiceToken] for a Minecraft access
     * token.
     *
     * @param[credentials] An active Xbox Live service token.
     * @return a token used to authenticate with protected Minecraft services.
     * @see[loginWithMicrosoft]
     *
     * @throws[AuthException] if the connection to Minecraft's authentication service fails.
     * @throws[AuthException] if Minecraft's authentication service returns a malformed or
     * incomplete response.
     * @throws[MinecraftAuthException] if Minecraft's authentication service returns a status code
     * that isn't between `200` and `299`, both included.
     */
    fun loginWithXbox(credentials: XboxLiveToken): MinecraftToken {
        val request = MinecraftXboxTokenRequest(credentials)

        val response = try {
            httpClient.send(request)
        } catch (cause: HttpException) {
            // Caught if the request itself fails.
            throw AuthException("Failed to request user credentials", cause)
        }

        // If the response code isn't 2xx, attempt to read the error's details.
        if (!response.isSuccess) {
            val error = try {
                response.asJsonObject().getString("errorType")
            } catch (cause: JsonMappingException) {
                null
            }

            throw MinecraftAuthException(error)
        }

        // Attempt to read the token from the response.
        return try {
            MinecraftToken(response.asJsonObject())
        } catch (cause: JsonMappingException) {
            // Caught if the response fails to parse as JSON.
            throw AuthException("Malformed response from Minecraft servers", cause)
        } catch (cause: IllegalArgumentException) {
            // Caught if the response parses, but doesn't contain required fields.
            throw AuthException("Minecraft did not send a valid token", cause)
        }
    }

    /**
     * Simplifies the login process by logging into Xbox Live internally, then exchanging that token
     * for a Minecraft access token.
     *
     * @param[microsoftToken] A valid access token for a Microsoft account.
     *
     * @throws[AuthException] if the connection to Minecraft's authentication service fails.
     * @throws[AuthException] if Minecraft's authentication service returns a malformed or
     * incomplete response.
     * @throws[AuthException] if the connection to the Xbox Live service fails.
     * @throws[AuthException] if Xbox Live returns a malformed or incomplete response.
     * @throws[XboxLiveAuthException] if Xbox Live returns a status code that isn't between `200`
     * and `299`, both included.
     * @throws[MinecraftAuthException] if Minecraft's authentication service returns a status code
     * that isn't between `200` and `299`, both included.
     *
     */
    fun loginWithMicrosoft(microsoftToken: String): MinecraftToken {

        val userToken = try {
            xboxClient.getUserToken(microsoftToken)
        } catch (cause: AuthException) {
            throw AuthException("Failed to fetch a user token", cause)
        }

        val xstsToken = try {
            xboxClient.getServiceToken(userToken.value)
        } catch (cause: AuthException) {
            throw AuthException("Failed to fetch a service token", cause)
        }

        return loginWithXbox(xstsToken)
    }
}