package me.nullicorn.msmca.xbox

import me.nullicorn.msmca.AuthException
import me.nullicorn.msmca.http.MockHttpClient
import me.nullicorn.msmca.mock.*
import me.nullicorn.msmca.util.*
import kotlin.js.JsName
import kotlin.math.PI
import kotlin.test.*

class XboxLiveAuthTests {

    private lateinit var http: MockHttpClient
    private lateinit var xbox: XboxLiveAuth

    @BeforeTest
    fun setUp() {
        http = MockHttpClient()
        xbox = XboxLiveAuth(http)
    }

    @Test
    @JsName(ONE)
    fun `should not throw when public constructor is called`() {
        assertSucceeds { XboxLiveAuth() }
    }

    @Test
    @JsName(TWO)
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
    @JsName(THREE)
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
    @JsName(FOUR)
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
    @JsName(FIVE)
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
    @JsName(SIX)
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
    @JsName(SEVEN)
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
    @JsName(EIGHT)
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
    @JsName(NINE)
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

    @Test
    @JsName(TEN)
    fun `should throw AuthException if response doesn't include a token`() {
        http.nextResponse = MockResponses.Xbox.withoutTokenInBody()

        assertFailsWith<AuthException> {
            xbox.getUserToken(MockTokens.SIMPLE)
        }

        assertFailsWith<AuthException> {
            xbox.getServiceToken(MockTokens.SIMPLE)
        }
    }

    @Test
    @JsName(ELEVEN)
    fun `should throw AuthException if response doesn't include a user hash`() {
        for (response in MockResponses.Xbox.manyWithoutUserHashInBody()) {
            http.nextResponse = response

            assertFailsWith<AuthException> {
                xbox.getUserToken(MockTokens.SIMPLE)
            }

            assertFailsWith<AuthException> {
                xbox.getServiceToken(MockTokens.SIMPLE)
            }
        }
    }

    @Test
    @JsName(TWELVE)
    fun `should return an XboxLiveToken with the same value and hash from the response's body`() {
        // User tokens
        val userToken = XboxLiveToken(
            value = MockTokens.SIMPLE,
            user = (PI * 100_000).toInt().toString(),
        )
        http.nextResponse = MutableResponse(
            token = userToken.value,
            userHash = userToken.user,
        ).toResponse()

        val actualUserToken = xbox.getUserToken("my.access.token")
        assertEquals(userToken.value, actualUserToken.value)
        assertEquals(userToken.user, actualUserToken.user)
        assertEquals(userToken, actualUserToken)

        // Service tokens.

        val serviceToken = XboxLiveToken(
            value = userToken.value.reversed(),
            user = userToken.user.reversed(),
        )
        http.nextResponse = MutableResponse(
            token = serviceToken.value,
            userHash = serviceToken.user,
        ).toResponse()

        val actualServiceToken = xbox.getServiceToken("your.access.token")
        assertEquals(serviceToken.value, actualServiceToken.value)
        assertEquals(serviceToken.user, actualServiceToken.user)
        assertEquals(serviceToken, actualServiceToken)
    }

    // TODO: 2/21/22 Test that the returned XboxLiveToken has the correct token & user hash.
}