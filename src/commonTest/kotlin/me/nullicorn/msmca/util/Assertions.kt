package me.nullicorn.msmca.util

/**
 * Ensures that a [test] does not raise any exceptions.
 *
 * If the test fails/throws, an [AssertionError] is thrown, detailing the cause of the exception.
 *
 * @param[description] An optional message indicating what the test is asserting. It should be in
 * the present tense, such as...
 * ```text
 * "get a random number"
 * ```
 *
 * @throws[AssertionError] if an exception was thrown by the [test].
 */
inline fun assertSucceeds(description: String? = null, test: () -> Unit) {
    try {
        test()
    } catch (cause: Throwable) {
        // If failed, include the provided description in the message.
        // Otherwise, use a generic message starter.
        var errMessage = if (description != null) {
            "Failed to $description (${cause::class.simpleName})"
        } else {
            "Unexpected ${cause::class.simpleName} thrown"
        }

        // Append the cause's message if available.
        // For some reason AssertionError doesn't accept a `cause` argument on multiplatform.
        if (cause.message != null)
            errMessage += ": ${cause.message}"

        throw AssertionError(errMessage)
    }
}