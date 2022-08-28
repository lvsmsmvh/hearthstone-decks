package com.cyberquick.hearthstonedecks.data.server.blizzard.hearthstone

import retrofit2.http.*

interface HearthstoneApi {
    @GET("hearthstone/deck")
//    @Headers("Authorization: Bearer US43vH18oM5AucyuaaqR9LdsnGEa8H4EOP")
    suspend fun getDeck(
        @Header("Authorization") token: String,
        @Query(":region") region: String = "eu",
        @Query("locale") locale: String = "en_EU",
        @Query("code") code: String,
    ): DeckResponse
}