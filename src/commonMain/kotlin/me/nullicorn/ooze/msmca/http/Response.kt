package me.nullicorn.ooze.msmca.http

import me.nullicorn.ooze.msmca.json.*

/**
 * Information sent by a server in response to an HTTP [request][Request].
 *
 * @param[status] The HTTP status code provided at the start of the response.
 * @param[headers] Any key-value pairs included at the start of the response.
 * @param[body] The main contents of the response.
 */
data class Response(
    val status: Int,
    val headers: Headers,
    val body: String,
) {
    /**
     * Whether the [status] code is in the range `200`-`299`, both inclusive.
     */
    val isSuccess = (200..299).contains(status)

    /**
     * Attempts to parse the response's [body] as a JSON object.
     *
     * If the response is blank (empty, or only whitespace), an empty JSON object is returned.
     *
     * @return the parsed object.
     *
     * @throws[JsonMappingException] if the response's body is not valid JSON, or if it is valid
     * JSON but not an *object*.
     *
     * @see[JsonMapper.parseObject]
     */
    internal fun asJsonObject(): JsonObjectView = if (body.isNotBlank()) {
        JsonMapper.parseObject(body)
    } else EmptyJsonObjectView

    /**
     * Attempts to parse the response's [body] as a JSON array.
     *
     * If the response is blank (empty, or only whitespace), an empty JSON array is returned.
     *
     * @return the parsed array.
     * @throws[JsonMappingException] if the response's body is not valid JSON, or if it is valid
     * JSON but not an *array*.
     *
     * @see[JsonMapper.parseArray]
     */
    internal fun asJsonArray(): JsonArrayView = if (body.isNotBlank()) {
        JsonMapper.parseArray(body)
    } else EmptyJsonArrayView
}