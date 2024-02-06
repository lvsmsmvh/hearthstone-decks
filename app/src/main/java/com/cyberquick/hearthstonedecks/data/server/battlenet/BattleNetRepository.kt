package com.cyberquick.hearthstonedecks.data.server.battlenet

import android.util.Log
import com.cyberquick.hearthstonedecks.data.server.battlenet.hearthstone.BattleNetApi
import com.cyberquick.hearthstonedecks.data.server.battlenet.oauth.OAuthApi
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.domain.entities.Expansion
import com.cyberquick.hearthstonedecks.domain.entities.ExpansionYear
import retrofit2.Call
import java.lang.Exception
import java.util.Locale
import javax.inject.Inject

class BattleNetRepository @Inject constructor(
    private val oAuthApi: OAuthApi,
    private val battleNetApi: BattleNetApi,
) {

    private var currentToken: String? = null

    suspend fun retrieveCards(code: String): Result<List<Card>> {
        Log.d("tag_network", "retrieveCards...")
        Log.d("tag_network", "getUserRegion = ${getUserRegion()}")
        Log.d("tag_network", "getUserLanguage = ${getUserLanguage()}")
        return doCall(
            createNetworkCall = {
                battleNetApi.getDeck(
                    token = "Bearer $currentToken",
                    region = getUserRegion(),
                    locale = getUserLanguage(),
                    code = code,
                )
            }
        ).map { deckResponse -> deckResponse.cards.sortedBy { it.manaCost } }
    }

    suspend fun retrieveSets(): Result<List<Expansion>> {
        return doCall(
            createNetworkCall = {
                battleNetApi.getSets(
                    token = "Bearer $currentToken",
                    region = getUserRegion(),
                    locale = getUserLanguage(),
                )
            }
        )
    }

    suspend fun retrieveSetGroups(): Result<List<ExpansionYear>> {
        return doCall(
            createNetworkCall = {
                battleNetApi.getSetGroups(
                    token = "Bearer $currentToken",
                    region = getUserRegion(),
                    locale = getUserLanguage(),
                )
            }
        )
    }

    private suspend fun <T> doCall(
        createNetworkCall: suspend () -> Call<T>
    ): Result<T> {
        if (currentToken == null) {
            currentToken = when (val tokenResult = getToken()) {
                is Result.Success -> tokenResult.data
                is Result.Error -> return Result.Error(tokenResult.exception)
            }
        }


        return when (val result = createNetworkCall().getResult()) {
            is Result.Success -> result
            else -> {
                when (val tokenResult = getToken()) {
                    is Result.Success -> {
                        currentToken = tokenResult.data
                        createNetworkCall().getResult()
                    }

                    is Result.Error -> Result.Error(tokenResult.exception)
                }
            }
        }
    }

    private suspend fun getToken(): Result<String> {
        return try {
            val token = oAuthApi.retrieveOAuth().access_token
            Result.Success(token)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Might be US, EU, KR, TW, CN
     */
    private fun getUserRegion(): String {
        return "eu"
    }

    private fun getUserLanguage(): String {
        return when (Locale.getDefault().language) {
            "de" -> "de_DE"
            "en" -> "en_US"
            "es" -> if (Locale.getDefault().country == "MX") "es_MX" else "es_ES"
            "fr" -> "fr_FR"
            "it" -> "it_IT"
            "ja" -> "ja_JP"
            "ko" -> "ko_KR"
            "pl" -> "pl_PL"
            "pt" -> if (Locale.getDefault().country == "BR") "pt_BR" else "pt_PT"
            "ru" -> "ru_RU"
            "th" -> "th_TH"
            "zh" -> if (Locale.getDefault().country == "TW") "zh_TW" else "zh_CN"
            else -> "en_US"
        }
    }

    private fun <T> Call<T>.getResult(): Result<T> {
        return try {
            val response = this.execute()
            return when (response.isSuccessful) {
                true -> Result.Success(response.body()!!)
                false -> Result.Error(Exception("${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}