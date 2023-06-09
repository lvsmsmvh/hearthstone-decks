package com.cyberquick.hearthstonedecks.data.server.battlenet

import com.cyberquick.hearthstonedecks.data.server.battlenet.hearthstone.BattleNetApi
import com.cyberquick.hearthstonedecks.data.server.battlenet.oauth.OAuthApi
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.domain.entities.Expansion
import com.cyberquick.hearthstonedecks.domain.entities.ExpansionYear
import java.lang.Exception
import javax.inject.Inject

class BattleNetRepository @Inject constructor(
    private val oAuthApi: OAuthApi,
    private val battleNetApi: BattleNetApi,
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
        val result = retrieveData { token ->
            return@retrieveData battleNetApi.getDeck(token = "Bearer $token", code = code)
        }
        result.asSuccess()?.let { deckResponse ->
            return Result.Success(deckResponse.data.cards.sortedBy { it.manaCost })
        }
        return Result.Error(result.asError()!!.exception)
    }

    suspend fun retrieveSets(): Result<List<Expansion>> {
        return retrieveData { token ->
            return@retrieveData battleNetApi.getSets(token = "Bearer $token")
        }
    }

    suspend fun retrieveSetGroups(): Result<List<ExpansionYear>> {
        return retrieveData { token ->
            return@retrieveData battleNetApi.getSetGroups(token = "Bearer $token")
        }
    }

    suspend fun <T> retrieveData(doCallWithToken: suspend (token: String) -> T): Result<T> {
        suspend fun withToken(token: String): Result<T> {
            return try {
                val result = doCallWithToken(token)
                Result.Success(result)
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

        return when (val result = withToken(currentToken!!)) {
            is Result.Success -> result
            else -> {
                when (val tokenResult = getToken()) {
                    is Result.Success -> {
                        currentToken = tokenResult.data
                        withToken(tokenResult.data)
                    }

                    is Result.Error -> Result.Error(tokenResult.exception)
                }
            }
        }
    }
}