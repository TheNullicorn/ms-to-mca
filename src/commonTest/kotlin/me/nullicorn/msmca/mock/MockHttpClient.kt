package me.nullicorn.msmca.mock

import me.nullicorn.msmca.http.HttpClient
import me.nullicorn.msmca.http.HttpException
import me.nullicorn.msmca.http.Request
import me.nullicorn.msmca.http.Response
import me.nullicorn.msmca.minecraft.MinecraftXboxTokenRequest
import me.nullicorn.msmca.xbox.XboxLiveTokenRequest
import kotlin.reflect.KClass

/**
 * A fake HTTP client whose responses can be manipulated based on the feature being tested.
 * @see[nextResponses]
 * @see[doThrowNext]
 */
class MockHttpClient : HttpClient {

    /**
     * Mock responses that the client should return when [send] receives a specific [Request] type.
     */
    val nextResponses = mutableMapOf<KClass<out Request>, Response>()

    /**
     * Whether the next call to [send] with raise an [HttpException], given the [Request] class.
     */
    var doThrowNext = mutableMapOf<KClass<out Request>, Boolean>()

    override fun send(request: Request): Response {
        if (doThrowNext[request::class] == true)
            throw HttpException("This is just a test")

        return nextResponses[request::class]
            ?: throw IllegalStateException("Missing mock response for ${request::class.simpleName}")
    }
}

private val XBOX_REQUEST_CLASS = XboxLiveTokenRequest::class
private val MINECRAFT_REQUEST_CLASS = MinecraftXboxTokenRequest::class

/**
 * The next response that should be returned by [MockHttpClient.send] when an [XboxLiveTokenRequest]
 * is sent.
 */
var MockHttpClient.nextXboxResponse: Response
    get() = nextResponses[XBOX_REQUEST_CLASS]!!
    set(value) {
        nextResponses[XBOX_REQUEST_CLASS] = value
    }

/**
 * Whether the next call to [MockHttpClient.send] should throw an [HttpException] if the request is
 * a [XboxLiveTokenRequest].
 */
var MockHttpClient.throwOnNextXboxRequest: Boolean
    get() = doThrowNext[XBOX_REQUEST_CLASS] ?: false
    set(value) {
        doThrowNext[XBOX_REQUEST_CLASS] = value
    }

/**
 * The next response that should be returned by [MockHttpClient.send] when an
 * [MinecraftXboxTokenRequest] is sent.
 */
var MockHttpClient.nextMinecraftResponse: Response
    get() = nextResponses[MINECRAFT_REQUEST_CLASS]!!
    set(value) {
        nextResponses[MINECRAFT_REQUEST_CLASS] = value
    }

/**
 * Whether the next call to [MockHttpClient.send] should throw an [HttpException] if the request is
 * a [MinecraftXboxTokenRequest].
 */
var MockHttpClient.throwOnNextMinecraftRequest: Boolean
    get() = doThrowNext[MINECRAFT_REQUEST_CLASS] ?: false
    set(value) {
        doThrowNext[MINECRAFT_REQUEST_CLASS] = value
    }