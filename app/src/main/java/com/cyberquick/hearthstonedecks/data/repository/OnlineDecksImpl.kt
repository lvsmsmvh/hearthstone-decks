package com.cyberquick.hearthstonedecks.data.repository

import com.cyberquick.hearthstonedecks.data.server.battlenet.BattleNetApiRepository
import com.cyberquick.hearthstonedecks.data.server.hearthpwn.HearthpwnApiRepository
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.*
import com.cyberquick.hearthstonedecks.domain.repositories.OnlineDecksRepository
import javax.inject.Inject

class OnlineDecksImpl @Inject constructor(
    private val battleNetApiRepository: BattleNetApiRepository,
    private val hearthpwnApiRepository: HearthpwnApiRepository,
) : OnlineDecksRepository {

    private var loadingPagesQuantity = false
    private var loadingPages = mutableSetOf<Int>()
    private var loadingDecks = mutableSetOf<DeckPreview>()
    private var loadingCards = mutableSetOf<Deck>()

    override suspend fun getPagesQuantity(): Result<Int> {
        loadingPagesQuantity = true
        val pages = hearthpwnApiRepository.getPagesQuantity()
        loadingPagesQuantity = false
        return pages
    }

    override suspend fun getPage(pageNumber: Int): Result<Page> {
        loadingPages.add(pageNumber)
        val page = hearthpwnApiRepository.getPage(pageNumber)
        loadingPages.remove(pageNumber)
        return page
    }

    override suspend fun getDeck(deckPreview: DeckPreview): Result<Deck> {
        loadingDecks.add(deckPreview)
        val deck = hearthpwnApiRepository.getDeck(deckPreview)
        loadingDecks.remove(deckPreview)
        return deck
    }

    override suspend fun getCards(deck: Deck): Result<List<Card>> {
        loadingCards.add(deck)
        val cards = battleNetApiRepository.retrieveCards(deck.code)
        loadingCards.remove(deck)
        return cards
    }
}