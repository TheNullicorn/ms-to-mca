package me.nullicorn.msmca.mock

import me.nullicorn.msmca.http.Response

/**
 * A mutable version of [Response].
 *
 * Useful for testing the library's handling of various HTTP responses from the services it used.
 */
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
) {
    /**
     * Creates an immutable copy of the response based on its current state.
     */
    fun toResponse() = Response(status, headers.toMap(), body)
}