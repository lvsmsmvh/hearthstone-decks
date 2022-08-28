package com.cyberquick.hearthstonedecks.data.db

import com.cyberquick.hearthstonedecks.data.db.dao.CardPreviewDao
import com.cyberquick.hearthstonedecks.data.db.dao.DeckDao
import com.cyberquick.hearthstonedecks.data.db.entities.FavoriteDecksEntity
import com.cyberquick.hearthstonedecks.data.db.mappers.DBMapper
import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.domain.entities.Page
import javax.inject.Inject
import kotlin.math.ceil

class RoomDBApi @Inject constructor(
    private val deckDao: DeckDao,
    private val cardPreviewDao: CardPreviewDao,
    private val dbMapper: DBMapper,
) {

    companion object {
        private const val ITEMS_ON_A_PAGE = 10
    }

    /**
     * Returns ID
     */
    fun insert(deck: Deck) {
        deckDao.insert(dbMapper.toDeckEntity(deck))
    }

    fun remove(deck: Deck) {
        deckDao.delete(deckDao.getDeckEntity(deck.deckPreview.id))
    }

    fun markFavorite(deck: Deck, favorite: Boolean) {
        val deckFavoriteEntity = deckDao.getFavoriteEntity(deck.deckPreview.id)

        if (favorite && deckFavoriteEntity == null) {
            deckDao.insert(FavoriteDecksEntity(deck.deckPreview.id))
        }

        if (!favorite && deckFavoriteEntity != null) {
            deckDao.delete(deckFavoriteEntity)
        }
    }

    fun getPagesQuantity(onlyFavorites: Boolean): Int {
        val total = when (onlyFavorites) {
            true -> deckDao.amountFavorites()
            false -> deckDao.amountAll()
        }
        return ceil(total / ITEMS_ON_A_PAGE.toFloat()).toInt()
    }

    fun isSaved(deckPreview: DeckPreview): Boolean {
        return deckDao.getDeckEntity(deckPreview.id) != null
    }

    fun isFavorite(deckPreview: DeckPreview): Boolean {
        deckDao.getDeckEntity(deckPreview.id)?.let {
            return deckDao.getFavoriteEntity(deckPreview.id) != null
        }
        return false
    }

    fun getPage(pageNumber: Int, onlyFavorites: Boolean): Page {
        val lastIndex = pageNumber * ITEMS_ON_A_PAGE
        val firstIndex = lastIndex - ITEMS_ON_A_PAGE + 1

        val entities = when (onlyFavorites) {
            true -> deckDao.getFavorites(firstIndex, lastIndex)
            false -> deckDao.getAll(firstIndex, lastIndex)
        }

        val deckPreviews = entities.map { dbMapper.toDeck(it).deckPreview }
        return Page(pageNumber, deckPreviews)
    }

    fun getDeck(deckPreview: DeckPreview): Deck? {
        return deckDao.getDeckEntity(deckPreview.id)?.let { dbMapper.toDeck(it) }
    }


    fun clearCache() {
        deckDao.deleteNotFavorites()
//        cardPreviewDao.deleteAllThatDoesNotRelateToDecks()
    }
}