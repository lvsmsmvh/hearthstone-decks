package com.cyberquick.hearthstonedecks.data.repository

import android.util.Log
import com.cyberquick.hearthstonedecks.data.server.battlenet.BattleNetApi
import com.cyberquick.hearthstonedecks.data.server.hearthpwn.HearthpwnApi
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.*
import com.cyberquick.hearthstonedecks.domain.repositories.OnlineDecksRepository
import javax.inject.Inject

class OnlineDecksImpl @Inject constructor(
    private val battleNetApi: BattleNetApi,
    private val hearthpwnApi: HearthpwnApi,
) : OnlineDecksRepository {

    override suspend fun getPage(pageNumber: Int): Result<Page> {
        return hearthpwnApi.getPage(pageNumber)
    }

    override suspend fun getDeck(deckPreview: DeckPreview): Result<Deck> {
        return when (val deckDetailsResult = hearthpwnApi.getDeckDetails(deckPreview)) {
            is Result.Error -> deckDetailsResult
            is Result.Success -> {
                Log.i("tag_niko", "Deck loaded")
                when (val cardsResult = battleNetApi.retrieveCards(deckDetailsResult.data.code)) {
                    is Result.Error -> cardsResult
                    is Result.Success -> {
                        Log.i("tag_niko", "Cards loaded")
                        Result.Success(Deck(
                            deckPreview,
                            deckDetailsResult.data.description,
                            deckDetailsResult.data.code,
                            cardsResult.data
                        ))
                    }
                }
            }
        }
    }
}