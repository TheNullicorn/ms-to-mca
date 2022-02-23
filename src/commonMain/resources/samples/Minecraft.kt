package samples

import me.nullicorn.msmca.AuthException
import me.nullicorn.msmca.minecraft.MinecraftAuth
import me.nullicorn.msmca.minecraft.MinecraftAuthException
import me.nullicorn.msmca.xbox.XboxLiveAuthException

object Minecraft {
    fun authViaMicrosoft() {
        /*
         * Make sure you have a valid access token for a Microsoft account.
         * ————————————————————————————————————————————————————————————————
         * An easy way to get this is using the Microsoft Authentication Library (MSAL). It should
         * look like a long string of random letters and numbers.
         */
        val microsoftAccessToken = "<access token received from MSAL>"

        /*
         * Create a Minecraft authentication client.
         * —————————————————————————————————————————
         * This class is pretty lightweight, but it's still good practice to only keep one instance
         * around if you'll be making a lot of requests.
         */
        val minecraft = MinecraftAuth()

        /*
         * Exchange the Microsoft access token for a Minecraft access token.
         * —————————————————————————————————————————————————————————————————
         * An XboxLiveAuthException may be thrown in a number of cases, such as...
         * - The Microsoft access token used is expired, or never was valid
         * - The Microsoft account isn't linked to Xbox Live
         * - The account's age is too young to access Xbox Live
         * - Xbox Live is not available in the account's country
         * - Xbox Live is experiencing an outage
         * It's recommended you display that reason to the user, preferably in a readable format.
         * You shouldn't need to log these exceptions.
         *
         * For more technical issues (typically related to the connection), a MinecraftAuthException
         * or AuthException will be thrown. If you encounter one of these, it's recommended you log
         * the exception and display a more vague message to the user, since AuthExceptions cover a
         * variety of issues.
         */
        val minecraftToken = try {
            minecraft.loginWithMicrosoft(microsoftAccessToken)

        } catch (cause: XboxLiveAuthException) {
            println("Failed to login to Minecraft; received an error from Xbox Live: ${cause.reason}")
            return

        } catch (cause: MinecraftAuthException) {
            println("Failed to login to Minecraft; received an error from Minecraft: ${cause.type}")
            return

        } catch (cause: AuthException) {
            println("Failed to login to Minecraft; connection failed: ${cause.message}")
            return
        }

        /*
         * Now you can access protected Minecraft services on behalf of the user, such as...
         * - Verifying their identity
         * - Changing their skin, cape, or username
         * - Log into online-mode servers with their account (especially for chat-bots)
         *
         * See https://wiki.vg/Mojang_API and https://wiki.vg/Protocol_Encryption for more info on
         * the endpoints available to you.
         *
         * If you're making those HTTP requests yourself, you'll need to include the token in the
         * "Authorization" header like so:
         *
         *      Authorization: type value
         *
         * ...where
         *      - "type" is the value of minecraftToken.type (usually "Bearer")
         *      - "value" is the value of minecraftToken.value (in the form of a JWT)
         */
    }
}