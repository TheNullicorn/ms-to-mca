package me.nullicorn.msmca.xbox

private typealias NumericCodes = MutableList<Long?>

/**
 * All numeric "XErr" codes that correspond to the current [XboxLiveError].
 *
 * May be empty.
 */
val XboxLiveError.numericCodes: NumericCodes
    get() = codesByError[this] ?: ArrayList(0)

private val codesByError = buildMap<XboxLiveError, NumericCodes> {
    for ((numericCode, error) in XboxLiveError.errorsByCode) {
        val existingCodes = getOrPut(error) { ArrayList() }
        existingCodes += numericCode
    }
}