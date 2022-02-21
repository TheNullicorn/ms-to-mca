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
    var token: String = MockTokens.SIMPLE,
    var userHash: String = "0",
    var body: String = """
            {
                "Token": "$token",
                "DisplayClaims": {
                    "xui": [
                        {
                            "uhs": "$userHash"
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