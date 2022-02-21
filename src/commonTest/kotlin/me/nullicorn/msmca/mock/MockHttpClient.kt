package me.nullicorn.msmca.mock

import me.nullicorn.msmca.http.HttpClient
import me.nullicorn.msmca.http.HttpException
import me.nullicorn.msmca.http.Request
import me.nullicorn.msmca.http.Response

/**
 * A fake HTTP client whose responses can be manipulated based on the feature being tested.
 * @see[nextResponse]
 * @see[nextThrows]
 */
class MockHttpClient : HttpClient {

    /**
     * The response that [send] will return when called next.
     *
     * This does not need to be assigned if [nextThrows] is set to `true` at the same time.
     */
    lateinit var nextResponse: Response

    /**
     * Whether the next call to [send] with raise an [HttpException].
     */
    var nextThrows = false

    override fun send(request: Request): Response {
        if (nextThrows)
            throw HttpException("This is just a test")

        return nextResponse
    }
}