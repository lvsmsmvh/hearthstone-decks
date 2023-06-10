package com.cyberquick.hearthstonedecks.data.db

import com.cyberquick.hearthstonedecks.data.db.dao.DeckDao
import com.cyberquick.hearthstonedecks.data.db.mappers.DBMapper
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.domain.entities.GetPageFilter
import com.cyberquick.hearthstonedecks.domain.entities.Page
import javax.inject.Inject
import kotlin.math.ceil

class RoomDBApi @Inject constructor(
    private val deckDao: DeckDao,
    private val dbMapper: DBMapper,
) {

    companion object {
        private const val ITEMS_ON_A_PAGE = 25
    }

    fun insert(deckPreview: DeckPreview) {
        deckDao.insert(dbMapper.toDeckEntity(deckPreview))
    }

    fun remove(deckPreview: DeckPreview) {
        deckDao.delete(deckDao.getDeckEntity(deckPreview.id))
    }

    fun isSaved(deckPreview: DeckPreview): Boolean {
        return deckDao.getDeckEntity(deckPreview.id) != null
    }

    fun getPage(pageNumber: Int, filter: GetPageFilter): Page {
        var totalPages = ceil(deckDao.amountDecks() / ITEMS_ON_A_PAGE.toFloat()).toInt()
        if (totalPages <1) totalPages = 1

        val pageNumberToLoad = when {
            pageNumber > totalPages -> totalPages
            pageNumber < 1 -> 1
            else -> pageNumber
        }

        val lastIndex = pageNumberToLoad * ITEMS_ON_A_PAGE - 1
        val firstIndex = lastIndex - ITEMS_ON_A_PAGE + 1

        val heroesNames = filter.heroes.map { it.nameInApi }

        val entities = deckDao.getDeckEntities(firstIndex, lastIndex)

        val deckPreviews = entities
            .filter { heroesNames.contains(it.gameClass) }
            .map { dbMapper.toDeckPreview(it) }
            .reversed()

        return Page(totalPages, pageNumberToLoad, deckPreviews)
    }
}