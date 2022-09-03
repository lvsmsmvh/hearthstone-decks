package com.cyberquick.hearthstonedecks.data.repository

import android.util.Log
import com.cyberquick.hearthstonedecks.data.db.RoomDBApi
import com.cyberquick.hearthstonedecks.domain.common.NoSavedDecksFoundException
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.*
import com.cyberquick.hearthstonedecks.domain.repositories.FavoriteDecksRepository
import java.lang.NullPointerException
import javax.inject.Inject

class FavoriteDecksImpl @Inject constructor(
    private val roomDBApi: RoomDBApi,
) : FavoriteDecksRepository {

    override suspend fun save(deck: Deck, cards: List<Card>): Result<Unit> {
        roomDBApi.insert(deck, cards)
        return Result.Success(Unit)
    }

    override suspend fun remove(deckPreview: DeckPreview): Result<Unit> {
        roomDBApi.remove(deckPreview)
        return Result.Success(Unit)
    }

    override suspend fun getPagesQuantity(): Result<Int> {
        val pages = roomDBApi.getPagesQuantity()
        return Result.Success(pages)
    }

    override suspend fun getPage(pageNumber: Int): Result<Page> {
        val page = roomDBApi.getPage(pageNumber)
        return when (page.deckPreviews.isEmpty()) {
            true -> Result.Error(NoSavedDecksFoundException())
            false -> Result.Success(page)
        }
    }

    override suspend fun getDeck(deckPreview: DeckPreview): Result<Deck> {
        val deck = roomDBApi.getDeck(deckPreview)
        val result = deck?.let { Result.Success(it) } ?: Result.Error(NullPointerException())
        return result.apply {
            Log.i("tag_db", "DB get deck result is ${this.simpleOutput()}")
        }
    }

    override suspend fun getCards(deck: Deck): Result<List<Card>> {
        val cards = roomDBApi.getCards(deck.deckPreview)
        val result = when (cards.isNotEmpty()) {
            true -> Result.Success(cards)
            false -> Result.Error(NullPointerException())
        }
        return result.apply {
            Log.i("tag_db", "DB get cards result is ${this.simpleOutput()}")
        }
    }

    override suspend fun isSaved(deckPreview: DeckPreview): Result<Boolean> {
        val isFavorite = roomDBApi.isSaved(deckPreview)
        return Result.Success(isFavorite)
    }
}