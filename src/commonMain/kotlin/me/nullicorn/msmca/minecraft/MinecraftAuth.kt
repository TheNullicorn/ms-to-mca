package me.nullicorn.msmca.minecraft

import me.nullicorn.msmca.AuthException
import me.nullicorn.msmca.http.BuiltInHttpClient
import me.nullicorn.msmca.http.HttpClient
import me.nullicorn.msmca.http.HttpException
import me.nullicorn.msmca.json.JsonMappingException
import me.nullicorn.msmca.xbox.XboxLiveAuth
import me.nullicorn.msmca.xbox.XboxLiveToken

/**
 * Provides methods for authenticating with Minecraft-related services.
 */
class MinecraftAuth(private val httpClient: HttpClient) {

    private val xbox = XboxLiveAuth(httpClient)

    constructor() : this(BuiltInHttpClient)

    /**
     * Exchanges an Xbox Live [service token][XboxLiveAuth.getServiceToken] for a Minecraft access
     * token.
     *
     * @param[credentials] An active Xbox Live service token.
     * @return a token used to authenticate with most Minecraft services.
     *
     * @throws[MinecraftAuthException] if Minecraft returns an error message or code.
     * @throws[AuthException] if the connection to Minecraft fails, or if the service returns a
     * malformed response.
     */
    fun login(credentials: XboxLiveToken): MinecraftToken {
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
     * @param[microsoftToken] A valid Microsoft access token.
     *
     * @throws[AuthException] if Xbox Live returns an error code.
     * @throws[AuthException] if Xbox Live or Minecraft fail to connect, or return a malformed
     * response.
     * @throws[MinecraftAuthException] if Minecraft returns an error code.
     */
    fun login(microsoftToken: String): MinecraftToken {

        val userToken = try {
            xbox.getUserToken(microsoftToken)
        } catch (cause: AuthException) {
            throw AuthException("Failed to fetch a user token", cause)
        }

        val xstsToken = try {
            xbox.getServiceToken(userToken.value)
        } catch (cause: AuthException) {
            throw AuthException("Failed to fetch a service token", cause)
        }

        return login(xstsToken)
    }
}