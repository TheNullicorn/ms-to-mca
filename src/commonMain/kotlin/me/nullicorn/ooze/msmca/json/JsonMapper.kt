package me.nullicorn.ooze.msmca.json

/**
 * Maps JSON to and from its string form.
 */
internal expect object JsonMapper {
    /**
     * Attempts to parse a string as JSON, with the assumption that it represents an object.
     *
     * @param[jsonString] The JSON compliant string to parse.
     * @return the parsed object.
     *
     * @throws[JsonMappingException] if the [jsonString], when parsed, does not represent an object.
     * @throws[JsonMappingException] if the [jsonString] is malformed.
     */
    fun parseObject(jsonString: String): JsonObjectView

    /**
     * Attempts to parse a string as JSON, with the assumption that it represents an array.
     *
     * @param[jsonString] The JSON compliant string to parse.
     * @return the parsed array.
     *
     * @throws[JsonMappingException] if the [jsonString], when parsed, does not represent an array.
     * @throws[JsonMappingException] if the [jsonString] is malformed.
     */
    fun parseArray(jsonString: String): JsonArrayView

    /**
     * Attempts to convert an object to an equivalent JSON representation.
     *
     * @param[input] The object to convert to JSON.
     * @return the object's JSON representation.
     *
     * @throws[JsonMappingException] if the [input] cannot be mapped to JSON.
     */
    fun stringify(input: Any?): String
}