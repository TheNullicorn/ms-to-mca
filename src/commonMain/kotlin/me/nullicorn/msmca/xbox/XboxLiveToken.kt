package me.nullicorn.msmca.xbox

/**
 * Credentials required to authenticate with Xbox Live and other linked services.
 * @param[value] The value of the token, used to authenticate with Xbox Live APIs.
 * @param[user] The "hash" of the user who the token was generated for. Not to be confused with the
 * user's actual ID, `XUID`.
 */
data class XboxLiveToken(
    val value: String,
    val user: String,
) {
    // Omit the token's "value" to prevent someone from accidentally logging it.
    override fun toString() = "XboxLiveToken(user='$user')"
}