package me.nullicorn.msmca.minecraft

import me.nullicorn.msmca.http.Request
import me.nullicorn.msmca.xbox.XboxLiveAuth
import me.nullicorn.msmca.xbox.XboxLiveToken

/**
 * Internal request used to authenticate with Minecraft using Xbox.
 *
 * @param[xboxToken] an Xbox Live [service token][XboxLiveAuth.getServiceToken].
 */
internal class MinecraftXboxTokenRequest(xboxToken: XboxLiveToken) : Request {
    override val method = "POST"

    override val url = "https://api.minecraftservices.com/authentication/login_with_xbox"

    override val headers: Map<String, String>
        get() = mapOf(
            "accept" to "application/json",
            "content-type" to "application/json",
        )

    override val body: Map<String, Any?> = mapOf(
        "identityToken" to "XBL3.0 x=${xboxToken.user};${xboxToken.value}"
    )
}