package me.nullicorn.msmca.mock

import me.nullicorn.msmca.http.Headers
import me.nullicorn.msmca.http.MockHttpClient
import me.nullicorn.msmca.http.Response

/**
 * Sample HTTP responses used to test how the library responds to various inputs from Microsoft and
 * Minecraft's services.
 *
 * This is meant to be used in conjunction with [MockHttpClient].
 */
object MockResponses {

    /**
     * Sample responses from Xbox Live services specifically.
     */
    object Xbox {

        val VALID = validExcept {
            // Don't modify; keeps the valid response as-is.
        }

        /**
         * Creates a valid response that shouldn't raise any errors from the library, but allows it
         * to be modified to test how the library response to various edge-cases in the response's
         * structure.
         *
         * @param[modifier] A function that tweaks the response's status, headers, and body.
         */
        fun validExcept(modifier: (MutableResponse) -> Unit) =
            MutableResponse().apply(modifier).let {
                Response(it.status, it.headers, it.body)
            }

        data class MutableResponse(
            var status: Int = 200,
            var headers: Headers = mutableMapOf(),
            var body: String = """
            {
                "Token": "${MockTokens.SIMPLE}",
                "DisplayClaims": {
                    "xui": [
                        {
                            "uhs": "0"
                        }
                    ]
                }
            }
        """.trimIndent(),
        )
    }
}