package me.nullicorn.msmca.xbox

import me.nullicorn.msmca.AuthException
import me.nullicorn.msmca.http.MockHttpClient
import me.nullicorn.msmca.mock.*
import me.nullicorn.msmca.util.*
import kotlin.js.JsName
import kotlin.test.*

class XboxLiveAuthTests {

    lateinit var http: MockHttpClient
    lateinit var xbox: XboxLiveAuth

    @BeforeTest
    fun setUp() {
        http = MockHttpClient()
        xbox = XboxLiveAuth(http)
    }

    @Test
    @JsName(ONE)
    fun `should throw AuthException if the request fails`() {
        http.nextThrows = true

        assertFailsWith(AuthException::class) {
            xbox.getUserToken(MockTokens.SIMPLE)
        }

        assertFailsWith(AuthException::class) {
            xbox.getServiceToken(MockTokens.SIMPLE)
        }
    }

    @Test
    @JsName(TWO)
    fun `should succeed if the response's status code is between 200 and 299`() {
        for (status in 200..299) {
            // Respond to all requests with a fake response.
            // Its status code is whichever one we're at in the loop.
            http.nextResponse = MockResponses.Xbox.validBut { response ->
                response.status = status
            }

            assertSucceeds("get user token when status=$status") {
                xbox.getUserToken(MockTokens.SIMPLE)
            }

            assertSucceeds("get service token when status=$status") {
                xbox.getServiceToken(MockTokens.SIMPLE)
            }
        }
    }

    @Test
    @JsName(THREE)
    fun `should throw XboxLiveAuthException if the response's status code is not between 200 and 299`() {
        for (status in 0..2000 step 3) {
            if (status in 200..299) continue

            http.nextResponse = MockResponses.Xbox.validBut { response ->
                response.status = status
            }

            assertFailsWith<XboxLiveAuthException> {
                xbox.getUserToken(MockTokens.SIMPLE)
            }

            assertFailsWith<XboxLiveAuthException> {
                xbox.getServiceToken(MockTokens.SIMPLE)
            }
        }
    }

    @Test
    @JsName(FOUR)
    fun `should throw AuthException if the response's body is not valid JSON`() {
        for (status in 200..299) {
            // Remove all quotes from the response JSON, thus invalidating it.
            http.nextResponse = MockResponses.Xbox.validBut { response ->
                response.body = response.body.replace("\"", "")
            }

            assertFailsWith<AuthException> {
                xbox.getUserToken(MockTokens.SIMPLE)
            }

            assertFailsWith<AuthException> {
                xbox.getServiceToken(MockTokens.SIMPLE)
            }
        }
    }

    @Test
    @JsName(FIVE)
    fun `should XboxLiveAuthException have the correct XboxLiveError if the response header has one`() {
        for ((numericCode, error) in XboxLiveError.errorsByCode) {
            http.nextResponse = MockResponses.Xbox.withErrorCodeInHeader(numericCode)

            val userException = assertFailsWith<XboxLiveAuthException> {
                xbox.getUserToken(MockTokens.SIMPLE)
            }

            val serviceException = assertFailsWith<XboxLiveAuthException> {
                xbox.getServiceToken(MockTokens.SIMPLE)
            }

            assertEquals(error, userException.reason)
            assertEquals(error, serviceException.reason)
        }
    }

    @Test
    @JsName(SIX)
    fun `should XboxLiveAuthException have the correct XboxLiveError if the response body has one`() {
        for ((numericCode, error) in XboxLiveError.errorsByCode) {
            http.nextResponse = MockResponses.Xbox.withErrorCodeInBody(numericCode)

            val userException = assertFailsWith<XboxLiveAuthException> {
                xbox.getUserToken(MockTokens.SIMPLE)
            }

            val serviceException = assertFailsWith<XboxLiveAuthException> {
                xbox.getServiceToken(MockTokens.SIMPLE)
            }

            assertEquals(error, userException.reason)
            assertEquals(error, serviceException.reason)
        }
    }

    @Test
    @JsName(SEVEN)
    fun `should XboxLiveAuthException's reason be MICROSOFT_TOKEN_INVALID if status is 401`() {
        http.nextResponse = MockResponses.Xbox.validBut { response ->
            response.status = 400
        }

        val userException = assertFailsWith<XboxLiveAuthException> {
            xbox.getUserToken(MockTokens.SIMPLE)
        }

        val serviceException = assertFailsWith<XboxLiveAuthException> {
            xbox.getServiceToken(MockTokens.SIMPLE)
        }

        assertEquals(XboxLiveError.MICROSOFT_TOKEN_INVALID, userException.reason)
        assertEquals(XboxLiveError.MICROSOFT_TOKEN_INVALID, serviceException.reason)
    }

    @Test
    @JsName(EIGHT)
    fun `should XboxLiveAuthException's reason be MICROSOFT_TOKEN_EXPIRED if status is 401`() {
        http.nextResponse = MockResponses.Xbox.validBut { response ->
            response.status = 401
        }

        val userException = assertFailsWith<XboxLiveAuthException> {
            xbox.getUserToken(MockTokens.SIMPLE)
        }

        val serviceException = assertFailsWith<XboxLiveAuthException> {
            xbox.getServiceToken(MockTokens.SIMPLE)
        }

        assertEquals(XboxLiveError.MICROSOFT_TOKEN_EXPIRED, userException.reason)
        assertEquals(XboxLiveError.MICROSOFT_TOKEN_EXPIRED, serviceException.reason)
    }

    // TODO: 2/21/22 Test responses with 2xx responses but no token or user hash.

    // TODO: 2/21/22 Test that the returned XboxLiveToken has the correct token & user hash.
}