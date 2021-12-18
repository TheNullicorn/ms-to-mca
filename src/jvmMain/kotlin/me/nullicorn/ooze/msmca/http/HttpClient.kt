package me.nullicorn.ooze.msmca.http

import me.nullicorn.ooze.msmca.util.isUrl
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

/**
 * Java implementation of an HTTP client, using the built-in [HttpURLConnection] class.
 */
internal actual object BuiltInHttpClient : HttpClient {

    actual override fun send(request: Request): Response {
        // Get request.url once so that this client can't be tricked by the getter.
        // (e.g. the getter returns a valid url for this check, but not for the actual request).
        val url = request.url
        if (!url.isUrl) throw IllegalArgumentException("Cannot request to malformed URL: $url")

        // Attempt to create a new connection to the request's URL.
        val connection = try {
            URL(url).openHttpConnection()
        } catch (cause: MalformedURLException) {
            throw HttpException("Invalid URL: \"$url\"", cause)
        }

        // Add the request's method, headers, and body to the connection.
        connection.configure(request)

        // Execute the request & read the response.
        return connection.response
    }
}

/**
 * Creates a connection to the URL using the HTTP or HTTPS protocol (whichever the URL
 * [specifies][URL.protocol]).
 *
 * This method does not [establish][HttpURLConnection.connect] the connection, just creates a new
 * connection instance to be configured.
 *
 * @throws HttpException if the URL does not use either the HTTP or HTTPS scheme.
 * @throws HttpException if the connection could not be created for some I/O-related reason
 * (specified in the exception's [cause][HttpException.cause]).
 */
private fun URL.openHttpConnection(): HttpURLConnection {
    val scheme = protocol.lowercase()
    if (scheme != "https" && scheme != "http") {
        throw HttpException("URL must use scheme https or http, not $scheme")
    }

    try {
        return openConnection() as HttpURLConnection

    } catch (cause: IOException) {
        // Caught if the connection could not be created.
        throw HttpException("Failed to open connection to $this", cause)

    } catch (cause: ClassCastException) {
        // Caught if the connection cannot be cast to HttpUrlConnection.
        throw HttpException("Connection created for wrong protocol", cause)
    }
}