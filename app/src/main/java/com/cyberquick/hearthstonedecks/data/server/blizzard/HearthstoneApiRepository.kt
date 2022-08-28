package com.cyberquick.hearthstonedecks.data.server.blizzard

import com.cyberquick.hearthstonedecks.data.server.blizzard.hearthstone.HearthstoneApi
import com.cyberquick.hearthstonedecks.data.server.blizzard.oauth.OAuthApi
import com.cyberquick.hearthstonedecks.data.server.blizzard.oauth.OAuthToken
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.Card
import java.lang.Exception
import javax.inject.Inject

class HearthstoneApiRepository @Inject constructor(
    private val hearthstoneApi: HearthstoneApi,
    private val oAuthApi: OAuthApi,
) {

    private var oauthToken: OAuthToken? = null

    private suspend fun getToken(): Result<String> {
        oauthToken?.let {
            if (it.isValid()) {
                return Result.Success(it.getToken())
            }
        }

        return try {
            val token = oAuthApi.retrieveOAuth().also {
                oauthToken = OAuthToken(it)
            }.access_token
            Result.Success(token)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun retrieveCards(code: String): Result<List<Card>> {
        val token = when (val curToken = getToken()) {
            is Result.Success -> curToken.data
            is Result.Error -> return curToken
        }

        return try {
            val deckResponse = hearthstoneApi.getDeck(
                token = "Bearer $token",
                code = code
            )
            Result.Success(deckResponse.cards)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}