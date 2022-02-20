package me.nullicorn.msmca.json

/**
 * Represents a JSON array with no elements.
 *
 * This is intended as a fallback for when the server's response is empty, but an array was
 * expected.
 */
internal object EmptyJsonArrayView : JsonArrayView {

    override val length = 0

    override fun get(index: Int): Nothing? = null

    override fun toString() = "[]"
}