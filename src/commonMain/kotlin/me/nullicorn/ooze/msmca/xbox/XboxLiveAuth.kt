package me.nullicorn.ooze.msmca.xbox

import me.nullicorn.ooze.msmca.AuthException
import me.nullicorn.ooze.msmca.http.BuiltInHttpClient
import me.nullicorn.ooze.msmca.http.HttpClient
import me.nullicorn.ooze.msmca.http.HttpException
import me.nullicorn.ooze.msmca.json.JsonMappingException

/**
 * Provides methods for authenticating with Xbox Live services.
 *
 * @see <a href="https://wiki.vg/Microsoft_Authentication_Scheme">wiki.vg - Microsoft Authentication
 * Scheme</a> - heavily referenced when writing this class.
 */
class XboxLiveAuth internal constructor(private val httpClient: HttpClient) {

    constructor() : this(BuiltInHttpClient)

    /**
     * Exchanges a Microsoft access token for an Xbox Live user token.
     *
     * @param[accessToken] A Microsoft access token, received from authentication.
     * @return credentials for the Xbox Live user.
     * @throws[AuthException] if the authentication request fails or returns an error.
     */
    fun getUserToken(accessToken: String) = getCredentials(
        request = XboxLiveTokenRequest.user(accessToken)
    )

    /**
     * Exchanges an Xbox Live user token for a service token.
     *
     * @param[userToken] An Xbox Live user token.
     * @return a service token for the token owner.
     * @throws[XboxLiveAuthException] if Xbox Live returned an error that should be displayed to the
     * end user. Check the exception's [reason][XboxLiveAuthException.reason] for specifics.
     * @throws[AuthException] if the authentication request fails or returns an error. This may
     * include problems or messages that end-users should not be shown directly.
     * @see[getUserToken]
     */
    fun getServiceToken(userToken: String) = getCredentials(
        request = XboxLiveTokenRequest.xsts(userToken)
    )

    /**
     * Internal logic shared between [getUserToken] and [getServiceToken], which use the exact
     * format for both the request and response.
     */
    private fun getCredentials(request: XboxLiveTokenRequest): XboxLiveToken {
        val response = try {
            httpClient.send(request)
        } catch (cause: HttpException) {
            // Caught if the request itself fails.
            throw AuthException("Failed to request user credentials", cause)
        }

        // If the response code isn't 2xx, attempt to read the error's details.
        val error = response.xboxLiveError
        if (error != null) throw XboxLiveAuthException(error)

        // Attempt to parse the response.
        val respJson = try {
            response.asJsonObject()
        } catch (cause: JsonMappingException) {
            // Caught if the response cannot be parsed as JSON.
            throw AuthException("Malformed response from Xbox servers", cause)
        }

        // Get the user/service token from the response.
        val token: String = respJson["Token"] as? String
            ?: throw AuthException("Xbox did not send an access token, or it was not a string")

        // Get the user hash from the response (from DisplayClaims.xui[0].uhs).
        val userHash: String = respJson.getObject("DisplayClaims")
            ?.getArray("xui")
            ?.getObject(0)
            ?.getString("uhs")
            ?: throw AuthException("Xbox did not send a user hash, or it was not a string")

        return XboxLiveToken(token, userHash)
    }
}

