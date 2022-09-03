package com.cyberquick.hearthstonedecks.data.server.battlenet.hearthstone

import retrofit2.http.*

interface DeckApi {
    @GET("hearthstone/deck")
    suspend fun getDeck(
        @Header("Authorization") token: String,
        @Query(":region") region: String = "eu",
        @Query("locale") locale: String = "en_EU",
        @Query("code") code: String,
    ): DeckResponse
}