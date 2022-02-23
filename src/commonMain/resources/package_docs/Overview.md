# Module ms-to-mca

Simplifies the process of logging into [Minecraft services](https://wiki.vg/Mojang_API) on behalf of
a Microsoft user. In order to use this library, you will need to create an app that interfaces with
the [Microsoft Identity Platform][ms-openid]. The easiest way is using an implementation of the
Microsoft Authentication Library ([MSAL][msal-overview]).

Once your app is set up, request an **access token** from the user (that's what you'll need to use this library). Be sure to ask for the `XboxLive.signin` scope! It's required for
logging in to Minecraft.

### Disclaimer

Firstly, **<ins>Minecraft access tokens can be dangerous!</ins>** Tokens grant an application **full permissions** to a user's account, meaning they can...
- connect to online-mode servers on the user's behalf
- change profile info...
    - username
    - capes
    - skins

I **strongly** recommend you provide a similar disclaimer to users logging into your own app, both for their security and to raise awareness of the dangers of Minecraft authentication with third-party apps.

Secondly, **<ins>this is NOT a Minecraft API library</ins>**. Its only function is exchanging Microsoft access tokens for Minecraft ones, which can be used to perform actions on behalf of a Minecraft account. To interface with the rest of Minecraft's online services you'll need a separate library of your choice.

[ms-openid]: https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-protocols-oidc

[msal-overview]: https://docs.microsoft.com/en-us/azure/active-directory/develop/msal-overview