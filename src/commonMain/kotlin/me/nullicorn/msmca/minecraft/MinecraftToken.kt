package me.nullicorn.msmca.minecraft

import me.nullicorn.msmca.json.JsonObjectView

/**
 * A token that can be used to access Minecraft services on a player's behalf.
 *
 * @param[type] The authorization scheme to be used with the token, such as "`Bearer`" or "`Basic`".
 * @param[value] The value of the token itself. Typically, this is in JSON Web Token (JWT) format.
 * @param[user] The UUID of the Minecraft account that the token is intended for. **This UUID is
 * associated with the account itself, not the player that belongs to the account.**
 * @param[duration] The amount of time, in seconds, that the token lasts for.
 */
data class MinecraftToken(
    val type: String,
    val value: String,
    val user: String,
    val duration: Int,
) {
    /**
     * Deserializes a JSON token received from Minecraft's Xbox login endpoint (see below).
     *
     * ```text
     * POST https://api.minecraftservices.com/authentication/login_with_xbox
     * ```
     *
     * @throws[IllegalArgumentException] if the [responseJson] is missing any required fields.
     */
    internal constructor(responseJson: JsonObjectView) : this(
        type = responseJson.getString("type") ?: "Bearer",

        value = responseJson.getString("access_token")
            ?: throw IllegalArgumentException("Token's value is missing"),

        user = responseJson.getString("username")
            ?: throw IllegalArgumentException("Token's user ID is missing"),

        duration = responseJson.getNumber("expires_in")?.toInt()
            ?: throw IllegalArgumentException("Token's duration is missing")
    )

    // Omit the token's "value" to prevent someone from accidentally logging it.
    override fun toString() = "MinecraftToken(type='$type', user='$user', duration=$duration)"
}