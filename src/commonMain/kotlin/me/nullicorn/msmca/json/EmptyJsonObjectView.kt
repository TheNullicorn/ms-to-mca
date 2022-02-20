package me.nullicorn.msmca.json

/**
 * Represents a JSON object with no entries.
 *
 * This is intended as a fallback for when the server's response is empty, but an object was
 * expected.
 */
internal object EmptyJsonObjectView : JsonObjectView {
    override val keys: Iterable<String> = emptySet()

    override fun get(key: String): Nothing? = null

    override fun toString() = "{}"
}