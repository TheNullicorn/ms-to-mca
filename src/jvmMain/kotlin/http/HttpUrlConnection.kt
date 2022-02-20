package me.nullicorn.msmca.http

import me.nullicorn.msmca.json.JsonMapper
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection

/*
 * Extensions for java.net.HttpURLConnection, allowing it to neatly be integrated into our internal
 * HTTP API.
 */

/**
 * Configures the connection to use the same [method][Request.method], [headers][Request.headers],
 * and [body][Request.body], if applicable, as the supplied request.
 *
 * @param[options] The request whose options should be applied to the connection.
 */
internal fun HttpURLConnection.configure(options: me.nullicorn.msmca.http.Request) {

    // Set the request's method (e.g. GET or POST).
    requestMethod = options.method.uppercase()

    // Set each of the request's headers, if present.
    for ((header, value) in options.headers) {
        setRequestProperty(header, value)
    }

    // Disable user interaction, since there's no "user" in this context.
    allowUserInteraction = false

    // Ignore redirect responses (status codes 3xx).
    instanceFollowRedirects = false

    connectTimeout = 4000
    readTimeout = 2000

    // Format the request's body as a JSON string, if present.
    val body = options.body
    if (body != null) {
        doOutput = true
        outputStream.writer().use { it.write(JsonMapper.stringify(body)) }
    }
}

/**
 * Opens the connection, then deserializes the response as a [Response] object.
 *
 * @throws[HttpException] if the connection fails, or if the server sends a malformed response.
 */
internal val HttpURLConnection.response: me.nullicorn.msmca.http.Response
    get() {
        // Connect, catching any errors that occur.
        val error = try {
            connect()
        } catch (cause: IOException) {
            // connect() throws just for HTTP status codes >= 400, even if the response is valid.
            // The only errors we care about are connection ones, which is why we check for a -1
            // status code below.
            cause
        }

        // If the connection didn't turn up a status code, then rethrow whatever connect() threw.
        if (responseCode == -1) {
            throw me.nullicorn.msmca.http.HttpException("HTTP request failed",
                cause = error as? Throwable)
        }

        // Parse the response headers & body, then wrap them in our Response class.
        return try {
            me.nullicorn.msmca.http.Response(responseCode, responseHeaders, responseBody)
        } catch (cause: IOException) {
            throw me.nullicorn.msmca.http.HttpException("HTTP response could not be parsed", cause)
        }
    }

/**
 * The first value sent for each header in the HTTP response.
 *
 * Should be used instead of [headerFields][HttpURLConnection.getHeaderFields], which returns *all*
 * values sent for each header.
 */
private val HttpURLConnection.responseHeaders: me.nullicorn.msmca.http.Headers
    get() = headerFields.let { original ->
        buildMap {
            for ((name, values) in original.entries) put(name ?: "", values.first())
        }
    }

/**
 * Reads the contents of the response body as plaintext.
 * @throws IOException if the body cannot not be read.
 */
private val HttpURLConnection.responseBody: String
    get() {
        // If the server sent nothing, return an empty string by default.
        //
        // errorStream returns null in this case, and inputStream may throw an exception, so this
        // check has to come first.
        if (contentLength == 0) return ""

        // Get a reader of the body, regardless of whether it's an error or not.
        val bodyReader = (errorStream ?: inputStream)?.reader()
        // Read all the body's text, then close the stream afterwards (via use()).
        return bodyReader?.use(InputStreamReader::readText) ?: ""
    }