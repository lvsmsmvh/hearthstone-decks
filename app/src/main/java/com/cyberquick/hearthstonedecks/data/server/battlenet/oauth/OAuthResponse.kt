package com.cyberquick.hearthstonedecks.data.server.battlenet.oauth

data class OAuthResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Long,
    val sub: String,
)