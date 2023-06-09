package com.cyberquick.hearthstonedecks.data.repository

import com.cyberquick.hearthstonedecks.data.db.RoomDBApi
import com.cyberquick.hearthstonedecks.domain.exceptions.NoSavedDecksFoundException
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

    override suspend fun getPage(pageNumber: Int, filter: GetPageFilter): Result<Page> {
        val page = roomDBApi.getPage(pageNumber, filter)
        return when (page.deckPreviews.isEmpty()) {
            true -> Result.Error(NoSavedDecksFoundException())
            false -> Result.Success(page)
        }
    }

    override suspend fun getDeck(deckPreview: DeckPreview): Result<Deck> {
        val deck = roomDBApi.getDeck(deckPreview)
        return deck?.let { Result.Success(it) } ?: Result.Error(NullPointerException())
    }

    override suspend fun isSaved(deckPreview: DeckPreview): Result<Boolean> {
        val isFavorite = roomDBApi.isSaved(deckPreview)
        return Result.Success(isFavorite)
    }
}