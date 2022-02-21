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
         * Creates a valid response that shouldn't raise any errors from the library, but allows it
         * to be modified to test how the library response to various edge-cases in the response's
         * structure.
         *
         * @param[modifier] A function that tweaks any combination of the response's status,
         * headers, and body.
         */
        fun validBut(modifier: (MutableResponse) -> Unit) =
            MutableResponse().apply(modifier).toResponse()

        /**
         * Modifies an otherwise valid response to include an `XErr` header, which the library
         * should raise an error for, interpreting the `XErr` as an [XboxLiveError] value.
         *
         * @param[error] The value to put in the `XErr` field.
         */
        fun withErrorCodeInHeader(error: Long?) = validBut { response ->
            response.status = 403
            response.headers["XErr"] = "$error"
        }

        /**
         * Modifies an otherwise valid response to include an `XErr` field in the JSON body, which
         * the library should raise an error for, interpreting the `XErr` as an [XboxLiveError]
         * value.
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
         * A response that is otherwise valid, but is missing the field with the necessary access
         * token.
         */
        fun withoutTokenInBody() = validBut { response ->
            response.body = """
                {
                    "DisplayClaims": {
                        "xui": [
                            {
                                "uhs": "0"
                            }
                        ]
                    }
                }
            """.trimIndent()
        }

        /**
         * A series of responses that are otherwise valid, but are missing the field with the user's
         * hash in the response body.
         */
        fun manyWithoutUserHashInBody(): List<Response> = arrayOf(
            """
                {
                    "Token": "${MockTokens.SIMPLE}"
                }
            """,
            """
                {
                    "Token": "${MockTokens.SIMPLE}",
                    "DisplayClaims": null
                }
            """,
            """
                {
                    "Token": "${MockTokens.SIMPLE}",
                    "DisplayClaims": {}
                }
            """,
            """
                {
                    "Token": "${MockTokens.SIMPLE}",
                    "DisplayClaims": {
                        "NoXuiInSight": true
                    }
                }
            """,
            """
                {
                    "Token": "${MockTokens.SIMPLE}",
                    "DisplayClaims": {
                        "xui": null
                    }
                }
            """,
            """
                {
                    "Token": "${MockTokens.SIMPLE}",
                    "DisplayClaims": {
                        "xui": []
                    }
                }
            """,
            """
                {
                    "Token": "${MockTokens.SIMPLE}",
                    "DisplayClaims": {
                        "xui": [ {} ]
                    }
                }
            """,
            """
                {
                    "Token": "${MockTokens.SIMPLE}",
                    "DisplayClaims": {
                        "xui": [
                            {
                                "NoHashHere": true
                            }
                        ]
                    }
                }
            """,
        ).map { body ->
            validBut { it.body = body.trimIndent() }
        }
    }
}