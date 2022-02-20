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
    internal constructor(json: JsonObjectView) : this(
        type = json.getString("type") ?: "Bearer",

        value = json.getString("access_token")
            ?: throw IllegalArgumentException("Token has no value"),

        user = json.getString("username")
            ?: throw IllegalArgumentException("Token has no intended user"),

        duration = json.getNumber("expires_in")?.toInt()
            ?: throw IllegalArgumentException("Token has no duration value")
    )

    // Omit the token's "value" to prevent someone from accidentally logging it.
    override fun toString() = "MinecraftToken(type='$type', user='$user', duration=$duration)"
}