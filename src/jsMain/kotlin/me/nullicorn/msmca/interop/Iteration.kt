package me.nullicorn.msmca.interop

internal external interface JsIterator<T> {
    fun next(): JsIteration<T>
}

internal external interface JsIteration<T> {
    val done: Boolean?
    val value: T?
}

internal inline fun <reified T> JsIterator<T>.toKtIterator() = object : Iterator<T?> {
    val jsIterator = this@toKtIterator
    var lastIteration: JsIteration<T>? = null

    override fun hasNext(): Boolean {
        return lastIteration?.done == false
    }

    override fun next(): T? {
        if (!hasNext()) throw NoSuchElementException("JS iterator is exhausted")

        lastIteration = jsIterator.next()

        // If the iterator is "done", throw unless the last iteration actually had a value.
        if (lastIteration?.done == true) {
            if (lastIteration?.value != null)
                return lastIteration?.value
            else throw NoSuchElementException("JS iterator is exhausted")
        }

        return lastIteration?.value?.unsafeCast<T>()
    }
}