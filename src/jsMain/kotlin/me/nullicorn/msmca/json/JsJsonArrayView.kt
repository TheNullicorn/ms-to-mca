package me.nullicorn.msmca.json

internal class JsJsonArrayView(private val actual: Array<*>) : JsonArrayView {
    override val length: Int
        get() = actual.size

    override fun get(index: Int) = actual[index]?.jsonView

    override fun toString() = JSON.stringify(actual)
}