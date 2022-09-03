package com.cyberquick.hearthstonedecks.data.repository

import com.cyberquick.hearthstonedecks.data.server.battlenet.BattleNetApiRepository
import com.cyberquick.hearthstonedecks.data.server.hearthpwn.HearthpwnApiRepository
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.*
import com.cyberquick.hearthstonedecks.domain.repositories.OnlineDecksRepository
import java.util.*
import javax.inject.Inject

class OnlineDecksImpl @Inject constructor(
    private val battleNetApiRepository: BattleNetApiRepository,
    private val hearthpwnApiRepository: HearthpwnApiRepository,
) : OnlineDecksRepository {


    override suspend fun getPagesQuantity(): Result<Int> {
        return hearthpwnApiRepository.getPagesQuantity()
    }

    override suspend fun getPage(pageNumber: Int): Result<Page> {
        return hearthpwnApiRepository.getPage(pageNumber)
    }

    override suspend fun getDeck(deckPreview: DeckPreview): Result<Deck> {
        val deck = hearthpwnApiRepository.getDeck(deckPreview)
        return deck
    }

    override suspend fun getCards(deck: Deck): Result<List<Card>> {
        val cards = battleNetApiRepository.retrieveCards(deck.code)
        return cards
    }
}