package me.nullicorn.msmca.json

/**
 * An unmodifiable view of a JSON object (aka a map or dictionary).
 */
internal interface JsonObjectView {

    /**
     * All valid keys that can be passed to any of this interface's [getters][get].
     */
    val keys: Iterable<String>

    /**
     * Retrieves the value within the object that's associated with the [key].
     *
     * For [strings][String], [numbers][Number], and [booleans][Boolean], (JSON "primitives") this
     * will return the value itself.
     *
     * However, for objects and arrays, this will return a [JsonObjectView] or [JsonArrayView]
     * respectively, which provides a layer of abstraction over the underlying structure.
     *
     * @return the value associated with the [key], or `null` if there is no value, or if it is
     * explicitly set to `null`.
     * @see[getObject]
     * @see[getArray]
     * @see[getNumber]
     * @see[getString]
     * @see[getBoolean]
     */
    operator fun get(key: String): Any?

    /**
     * Shorthand function for `get(key) as? JsonObjectView`.
     * @see[get]
     */
    fun getObject(key: String): JsonObjectView? = get(key) as? JsonObjectView

    /**
     * Shorthand function for `get(key) as? JsonArrayView`.
     * @see[get]
     */
    fun getArray(key: String): JsonArrayView? = get(key) as? JsonArrayView

    /**
     * Shorthand function for `get(key) as? Number`.
     * @see[get]
     */
    fun getNumber(key: String): Number? = get(key) as? Number

    /**
     * Shorthand function for `get(key) as? String`.
     * @see[get]
     */
    fun getString(key: String): String? = get(key) as? String

    /**
     * Shorthand function for `get(key) as? Boolean`.
     * @see[get]
     */
    fun getBoolean(key: String): Boolean? = get(key) as? Boolean

    override fun toString(): String
}