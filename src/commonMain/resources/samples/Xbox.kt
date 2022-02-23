package samples

import me.nullicorn.msmca.AuthException
import me.nullicorn.msmca.xbox.XboxLiveAuth
import me.nullicorn.msmca.xbox.XboxLiveAuthException

object Xbox {
    /**
     * A sample demonstrating the combined usage of the classes in `me.nullicorn.msmca.xbox`.
     */
    fun authViaMicrosoft() {
        /*
         * Make sure you have a valid access token for a Microsoft account.
         * ————————————————————————————————————————————————————————————————
         * An easy way to get this is using the Microsoft Authentication Library (MSAL). It should
         * look like a long string of random letters and numbers.
         */
        val microsoftAccessToken = "<access token received from MSAL>"

        /*
         * Create an Xbox Live authentication client.
         * ——————————————————————————————————————————
         * This class is pretty lightweight, but it's still good practice to only keep one instance
         * around if you'll be making a lot of requests.
         */
        val xbox = XboxLiveAuth()


        /*
         * Exchange the Microsoft access token for an Xbox Live "user token".
         * ——————————————————————————————————————————————————————————————————
         * An XboxLiveAuthException may be thrown in a number of cases, such as...
         * - The token used is expired, or never was valid
         * - The Microsoft account isn't linked to Xbox Live
         * - The account's age is too young to access Xbox Live
         * - Xbox Live is not available in the account's country
         * - Xbox Live is experiencing an outage
         * It's recommended you display that reason to the user, preferably in a readable format.
         * You shouldn't need to log these exceptions.
         *
         * For more technical issues (typically related to the connection), an AuthException is
         * thrown. If you encounter one of these, it's recommended you log the exception and display
         * a more vague message to the user, since AuthExceptions cover a variety of issues.
         */
        val xboxUserToken = try {
            xbox.getUserToken(microsoftAccessToken)

        } catch (cause: XboxLiveAuthException) {
            println("Failed to get user token; Xbox Live returned an error: ${cause.reason}")
            return

        } catch (cause: AuthException) {
            println("Failed to get user token; connection failed: ${cause.message}")
            return
        }

        /*
         * Similar to the previous method, but now we're exchanging the "user token" for a
         * "service token".
         * ———————————————————————————————————————————————————————————————————————————————
         * This token is what you'll send to Minecraft's servers in order to get a Minecraft access
         * token, which you can finally use to access protected Minecraft services.
         *
         * For that side of things, see the samples in the "me.nullicorn.msmca.minecraft" package.
         */
        val xboxServiceToken = try {
            xbox.getServiceToken(xboxUserToken.value)

        } catch (cause: XboxLiveAuthException) {
            println("Failed to get service token; Xbox Live returned an error: ${cause.reason}")
            return

        } catch (cause: AuthException) {
            println("Failed to get service token; connection failed: ${cause.message}")
            return
        }
    }
}