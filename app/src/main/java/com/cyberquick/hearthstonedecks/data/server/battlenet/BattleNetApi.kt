package com.cyberquick.hearthstonedecks.data.server.battlenet

import com.cyberquick.hearthstonedecks.data.server.battlenet.hearthstone.DeckApi
import com.cyberquick.hearthstonedecks.data.server.battlenet.oauth.OAuthApi
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.Card
import java.lang.Exception
import javax.inject.Inject

class BattleNetApi @Inject constructor(
    private val deckApi: DeckApi,
    private val oAuthApi: OAuthApi,
) {

    private var currentToken: String? = null

    private suspend fun getToken(): Result<String> {
        return try {
            val token = oAuthApi.retrieveOAuth().access_token
            Result.Success(token)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun retrieveCards(code: String): Result<List<Card>> {
        suspend fun getCards(token: String): Result<List<Card>> {
            return try {
                val deckResponse = deckApi.getDeck(
                    token = "Bearer $token",
                    code = code
                )
                Result.Success(deckResponse.cards)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

        if (currentToken == null) {
            currentToken = when (val tokenResult = getToken()) {
                is Result.Success -> tokenResult.data
                is Result.Error -> return Result.Error(tokenResult.exception)
            }
        }

        return when (val result = getCards(currentToken!!)) {
            is Result.Success -> result
            else -> {
                when (val tokenResult = getToken()) {
                    is Result.Success -> {
                        currentToken = tokenResult.data
                        getCards(tokenResult.data)
                    }
                    is Result.Error -> Result.Error(tokenResult.exception)
                }
            }
        }
    }
}