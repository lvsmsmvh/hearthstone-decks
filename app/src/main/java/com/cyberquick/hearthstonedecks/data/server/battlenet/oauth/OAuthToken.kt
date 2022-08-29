package com.cyberquick.hearthstonedecks.data.server.battlenet.oauth

class OAuthToken(
    private val oAuthResponse: OAuthResponse,
    private val retrievedTimeMs: Long = System.currentTimeMillis()
) {
    fun isValid(): Boolean {
        val passedMs = System.currentTimeMillis() - retrievedTimeMs
        val passedSeconds = (passedMs / 1000).toInt()
        return passedSeconds < oAuthResponse.expires_in
    }

    fun getToken() = oAuthResponse.access_token
}