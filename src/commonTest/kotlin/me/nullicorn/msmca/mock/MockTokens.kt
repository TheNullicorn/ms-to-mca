package me.nullicorn.msmca.mock

/**
 * Sample OAuth tokens (usually JWTs) that can be passed to the lubrary to test its handling of
 * them.
 */
object MockTokens {
    // Decoded is just "header.payload.signature"
    const val SIMPLE = "aGVhZGVy.cGF5bG9hZA.c2lnbmF0dXJl"
}