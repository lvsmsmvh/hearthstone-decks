package com.cyberquick.hearthstonedecks.data.db

import com.cyberquick.hearthstonedecks.data.db.dao.DeckDao
import com.cyberquick.hearthstonedecks.data.db.entities.DeckToCardEntity
import com.cyberquick.hearthstonedecks.data.db.mappers.DBMapper
import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.domain.entities.Page
import javax.inject.Inject
import kotlin.math.ceil

class RoomDBApi @Inject constructor(
    private val deckDao: DeckDao,
    private val dbMapper: DBMapper,
) {

    companion object {
        private const val ITEMS_ON_A_PAGE = 10
    }

    fun insert(deck: Deck, cards: List<Card>) {
        deckDao.insert(dbMapper.toDeckEntity(deck))
        cards.forEach { card ->
            deckDao.insert(dbMapper.toCardEntity(card))
            deckDao.insert(DeckToCardEntity(
                deckId = deck.deckPreview.id, cardId = card.id
            ))
        }
    }

    fun remove(deckPreview: DeckPreview) {
        deckDao.delete(deckDao.getDeckEntity(deckPreview.id))
        deckDao.deleteCardsThatDoesNotHaveDeck()
    }

    fun getPagesQuantity(): Int {
        var pages = ceil(deckDao.amountDecks() / ITEMS_ON_A_PAGE.toFloat()).toInt()
        if (pages == 0) pages++
        return pages
    }

    fun isSaved(deckPreview: DeckPreview): Boolean {
        return deckDao.getDeckEntity(deckPreview.id) != null
    }

    fun getPage(pageNumber: Int): Page {
        val lastIndex = pageNumber * ITEMS_ON_A_PAGE - 1
        val firstIndex = lastIndex - ITEMS_ON_A_PAGE + 1

        val entities = deckDao.getDeckEntities(firstIndex, lastIndex)
        val deckPreviews = entities.map { dbMapper.toDeck(it).deckPreview }
        return Page(pageNumber, deckPreviews)
    }

    fun getDeck(deckPreview: DeckPreview): Deck? {
        return deckDao.getDeckEntity(deckPreview.id)?.let { dbMapper.toDeck(it) }
    }

    fun getCards(deckPreview: DeckPreview): List<Card> {
        return deckDao.getCardsForDeckId(deckPreview.id).map { dbMapper.toCard(it) }
    }
}