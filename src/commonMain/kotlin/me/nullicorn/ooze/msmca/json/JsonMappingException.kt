package me.nullicorn.ooze.msmca.json

/**
 * Indicates that a JSON string could not be deserialized, or vice versa.
 */
internal class JsonMappingException(
    override val message: String?,
    override val cause: Throwable? = null,
) : Exception()