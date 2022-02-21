# Minecraft Login using Microsoft

Simplifies the process of logging into [Minecraft services](https://wiki.vg/Mojang_API) on behalf of
a Microsoft user. In order to use this library, you will need to create an app that interfaces with
the [Microsoft Identity Platform][ms-openid]. The easiest way is using an implementation of the
Microsoft Authentication Library ([MSAL][msal-overview]).

Once your app is set up, request an **access token** from the user (that's what you'll need to use this library). Be sure to ask for the `XboxLive.signin` scope! It's required for
logging in to Minecraft.

# 🚨 <ins>Disclaimer</ins> 🚨

Firstly, **<ins>Minecraft access tokens can be dangerous!</ins>** Tokens grant an application **full permissions** to a user's account, meaning they can...
- connect to online-mode servers on the user's behalf
- change profile info...
  - username
  - capes
  - skins

I **strongly** recommend you provide a similar disclaimer to users logging into your own app, both for their security and to raise awareness of the dangers of Minecraft authentication with third-party apps.

Secondly, **<ins>this is NOT a Minecraft API library</ins>**. Its only function is exchanging Microsoft access tokens for Minecraft ones, which can be used to perform actions on behalf of a Minecraft account. To interface with the rest of Minecraft's online services you'll need a separate library of your choice.

## Usage

### Quick Login

This process takes you straight from Microsoft --> Minecraft token, without dealing with Xbox by
hand.

```kotlin
// Authentication with Minecraft is done via the MinecraftAuth class.
val minecraft = MinecraftAuth()

// Exchanges your Microsoft access token for a Minecraft access token.
val token: MinecraftToken = minecraft.login("<microsoft access token>")
```

A `MinecraftToken` has four attributes:

- `type` (String) The authentication scheme to use the token with.
    - "scheme" refers to the word before the token when used in an `Authorization` header.
    - e.g. if the scheme is `Bearer`, the header's value would look like `Bearer <VALUE>`<br><br>
- `value` (String) The actual value of the token.
    - This *should* be a valid JWT, but may not be if Minecraft ever decides to change the type of
      token it uses.<br><br>
- `user` (String) The UUID of the Minecraft account that the token is for.
    - This is **not** the same as the UUID of the player that belongs to the account.
    - This UUID *does* have hyphens, unlike UUIDs in other parts of Minecraft's APIs.<br><br>
- `duration` (Int) The number of seconds that the token will last for after its creation.

### Login via Xbox Live

This process is slightly more involved, but lets you access the two intermediary tokens for Xbox
Live, if you need them for whatever reason.

If you already have an Xbox Live token for some reason, this also allows you to use it directly for
login.

```kotlin
// Xbox Live services are interacted with using the XboxLiveAuth class.
val xbox = XboxLiveAuth()

// The first step is to exchange your Microsoft access token for an Xbox Live "user" token.
val userToken = xbox.getUserToken("<microsoft access token>")

// The next step is to exchange your user token for an Xbox Live "service" token. This can also be
// referred to as an "XSTS token".
val serviceToken = xbox.getServiceToken(userToken.value)

// Now, you can get the same MinecraftToken object as in the "Quick Login".
val minecraft = MinecraftAuth()
val minecraftToken = minecraft.login(serviceToken)
```

[ms-openid]: https://docs.microsoft.com/en-us/azure/active-directory/develop/v2-protocols-oidc

[msal-overview]: https://docs.microsoft.com/en-us/azure/active-directory/develop/msal-overview
