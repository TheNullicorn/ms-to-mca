package me.nullicorn.msmca.mock

import me.nullicorn.msmca.http.MockHttpClient
import me.nullicorn.msmca.http.Response
import me.nullicorn.msmca.xbox.XboxLiveError

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

        /**
         * Mocks a typical response from an Xbox Service, which the library should not raise any
         * errors for.
         */
        fun valid() = validBut {
            // Don't modify; keeps the valid response as-is.
        }

        /**
         * Modifies the [valid] response to include an `XErr` header, which the library should raise
         * an error for, interpreting it as an [XboxLiveError].
         *
         * @param[error] The value to put in the `XErr` header.
         */
        fun withErrorCodeInHeader(error: Long?) = validBut { response ->
            response.status = 403
            response.headers["XErr"] = "$error"
        }

        /**
         * Modifies the [valid] response to include an `XErr` field in the JSON body, which the
         * library should raise an error for, interpreting it as an [XboxLiveError].
         *
         * @param[error] The value to put in the `XErr` field.
         */
        fun withErrorCodeInBody(error: Long?) = validBut { response ->
            response.status = 403
            response.body = """
                {
                    "XErr": $error
                }
            """.trimIndent()
        }

        /**
         * Creates a valid response that shouldn't raise any errors from the library, but allows it
         * to be modified to test how the library response to various edge-cases in the response's
         * structure.
         *
         * @param[modifier] A function that tweaks any combination of the response's status,
         * headers, and body.
         */
        fun validBut(modifier: (MutableResponse) -> Unit) =
            MutableResponse().apply(modifier).let {
                Response(it.status, it.headers, it.body)
            }

        data class MutableResponse(
            var status: Int = 200,
            var headers: MutableMap<String, String> = mutableMapOf(),
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