package me.nullicorn.ooze.msmca.json

/**
 * An unmodifiable view of a JSON array.
 */
internal interface JsonArrayView : Iterable<Any?> {

    /**
     * The number of elements in the array.
     */
    val length: Int

    /**
     * Retrieves the array element at the specified [index].
     *
     * For [strings][String], [numbers][Number], and [booleans][Boolean], (JSON "primitives") this
     * will return the value itself.
     *
     * However, for objects and arrays, this will return a [JsonObjectView] or [JsonArrayView]
     * respectively, which provides a layer of abstraction over the underlying structure.
     *
     * @param[index] The zero-based array index for the desired element.
     * @return the element at that index. May be null if the [index] is out of bounds, or if the
     * element at that [index] is explicitly set to `null`.
     * @see[getObject]
     * @see[getArray]
     * @see[getNumber]
     * @see[getString]
     * @see[getBoolean]
     */
    operator fun get(index: Int): Any?

    /**
     * Shorthand function for `get(index) as? JsonObjectView`.
     * @see[get]
     */
    fun getObject(index: Int): JsonObjectView? = get(index) as? JsonObjectView

    /**
     * Shorthand function for `get(index) as? JsonArrayView`.
     * @see[get]
     */
    fun getArray(index: Int): JsonArrayView? = get(index) as? JsonArrayView

    /**
     * Shorthand function for `get(index) as? Number`.
     * @see[get]
     */
    fun getNumber(index: Int): Number? = get(index) as? Number

    /**
     * Shorthand function for `get(index) as? String`.
     * @see[get]
     */
    fun getString(index: Int): String? = get(index) as? String

    /**
     * Shorthand function for `get(index) as? Boolean`.
     * @see[get]
     */
    fun getBoolean(index: Int): Boolean? = get(index) as? Boolean

    override fun iterator(): Iterator<Any?> {
        return object : Iterator<Any?> {
            private var index = 0

            override fun hasNext() = index < length

            override fun next() = if (hasNext()) get(index++)
            else throw NoSuchElementException("No more elements in iterator")
        }
    }

    override fun toString(): String
}