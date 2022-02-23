package me.nullicorn.msmca.xbox

import me.nullicorn.msmca.json.JsonObjectView

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
    /**
     * Deserializes a JSON token received from an Xbox Live authentication service.
     *
     * @throws[IllegalArgumentException] if the [responseJson] is missing any required fields.
     */
    internal constructor(responseJson: JsonObjectView) : this(
        value = responseJson.tokenValue
            ?: throw IllegalArgumentException("Token's value is missing"),

        user = responseJson.userHash
            ?: throw IllegalArgumentException("Token's user hash is missing")
    )

    // Omit the token's "value" to prevent someone from accidentally logging it.
    override fun toString() = "XboxLiveToken(user='$user')"

    private companion object {

        /**
         * Retrieves the acces token value from the following JSON path in the response:
         * ```text
         * Token
         * ```
         * May be `null` if the value is missing, or if it isn't a string.
         */
        val JsonObjectView.tokenValue: String?
            get() = getString("Token")

        /**
         * Retrieves the user hash value (`"uhs"`) from the following JSON path in the response:
         * ```text
         * DisplayClaims.xui[i].uhs
         * ```
         * ...Where `i` is the index of the first object in `xui` that contains a string field
         * named `uhs`
         *
         * May be `null` if the value (or any of its parent elements) are missing, or if it isn't a
         * string.
         */
        val JsonObjectView.userHash: String?
            get() {
                val xuiClaims = getObject("DisplayClaims")?.getArray("xui")
                    ?: return null

                for (i in 0 until xuiClaims.length)
                    return xuiClaims.getObject(i)?.getString("uhs") ?: continue

                return null
            }
    }
}