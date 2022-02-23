package me.nullicorn.msmca.xbox

import me.nullicorn.msmca.AuthException
import me.nullicorn.msmca.http.BuiltInHttpClient
import me.nullicorn.msmca.http.HttpClient
import me.nullicorn.msmca.http.HttpException
import me.nullicorn.msmca.json.JsonMappingException
import me.nullicorn.msmca.xbox.XboxLiveError.Companion.xboxLiveError

/**
 * Provides methods for authenticating with Xbox Live services.
 *
 * @see <a href="https://wiki.vg/Microsoft_Authentication_Scheme">wiki.vg - Microsoft Authentication
 * Scheme</a> - heavily referenced when writing this class.
 */
class XboxLiveAuth(private val httpClient: HttpClient) {

    constructor() : this(BuiltInHttpClient)

    /**
     * Exchanges a Microsoft access token for an Xbox Live user token.
     *
     * @param[accessToken] A Microsoft access token, received from authentication.
     * @return credentials for the Xbox Live user.
     * @see[getServiceToken]
     *
     * @throws[AuthException] if the connection to the Xbox Live service fails.
     * @throws[AuthException] if Xbox Live returns a malformed or incomplete response body.
     * @throws[XboxLiveAuthException] if Xbox Live returns a status code that isn't between `200`
     * and `299`, both included.
     */
    fun getUserToken(accessToken: String) = getCredentials(
        request = XboxLiveTokenRequest.user(accessToken)
    )

    /**
     * Exchanges an Xbox Live user token for a service token.
     *
     * @param[userToken] An Xbox Live user token.
     * @return a service token for the token owner.
     * @see[getUserToken]
     *
     * @throws[AuthException] if the connection to the Xbox Live service fails.
     * @throws[AuthException] if Xbox Live returns a malformed or incomplete response body.
     * @throws[XboxLiveAuthException] if Xbox Live returns a status code that isn't between `200`
     * and `299`, both included.
     */
    fun getServiceToken(userToken: String) = getCredentials(
        request = XboxLiveTokenRequest.xsts(userToken)
    )

    /**
     * Internal logic shared between [getUserToken] and [getServiceToken], which use the exact
     * format for both the request and response.
     *
     * @throws[AuthException] if the connection to the Xbox Live service fails.
     * @throws[AuthException] if Xbox Live returns a malformed or incomplete response body.
     * @throws[XboxLiveAuthException] if Xbox Live returns a status code that isn't between `200`
     * and `299`, both included.
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
            throw AuthException("Malformed response from Xbox Live service", cause)
        }

        return try {
            XboxLiveToken(respJson)
        } catch (cause: IllegalArgumentException) {
            throw AuthException("Incomplete response from Xbox Live service", cause)
        }
    }
}

