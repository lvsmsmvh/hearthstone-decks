package com.cyberquick.hearthstonedecks.data.server.battlenet.hearthstone

import com.cyberquick.hearthstonedecks.domain.entities.Expansion
import com.cyberquick.hearthstonedecks.domain.entities.ExpansionYear
import retrofit2.http.*

interface BattleNetApi {
    @GET("hearthstone/deck")
    suspend fun getDeck(
        @Header("Authorization") token: String,
        @Query(":region") region: String = "eu",
        @Query("locale") locale: String = "en_EU",
        @Query("code") code: String,
    ): DeckResponse

    @GET("/hearthstone/metadata/sets")
    suspend fun getSets(
        @Header("Authorization") token: String,
        @Query(":region") region: String = "eu",
        @Query("locale") locale: String = "en_EU",
    ): List<Expansion>

    @GET("/hearthstone/metadata/setGroups")
    suspend fun getSetGroups(
        @Header("Authorization") token: String,
        @Query(":region") region: String = "eu",
        @Query("locale") locale: String = "en_EU",
    ): List<ExpansionYear>
}