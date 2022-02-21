package me.nullicorn.msmca.xbox

import me.nullicorn.msmca.AuthException
import me.nullicorn.msmca.http.MockHttpClient
import me.nullicorn.msmca.mock.MockTokens
import me.nullicorn.msmca.mock.MockResponses
import me.nullicorn.msmca.util.assertSucceeds
import kotlin.js.JsName
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class XboxLiveAuthTests {

    lateinit var http: MockHttpClient
    lateinit var xbox: XboxLiveAuth

    @BeforeTest
    fun setUp() {
        http = MockHttpClient()
        xbox = XboxLiveAuth(http)
    }

    @Test
    @JsName("_")
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
    @JsName("__")
    fun `should succeed if the response's status code is between 200 and 299`() {
        for (status in 200..299) {
            // Respond to all requests with a fake response.
            // Its status code is whichever one we're at in the loop.
            http.nextResponse = MockResponses.Xbox.validExcept { response ->
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
}