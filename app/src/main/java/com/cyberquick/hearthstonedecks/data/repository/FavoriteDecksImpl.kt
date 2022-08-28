package com.cyberquick.hearthstonedecks.data.repository

import com.cyberquick.hearthstonedecks.data.db.RoomDBApi
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.*
import com.cyberquick.hearthstonedecks.domain.repositories.FavoriteDecksRepository
import java.lang.NullPointerException
import javax.inject.Inject

class FavoriteDecksImpl @Inject constructor(
    private val roomDBApi: RoomDBApi,
) : FavoriteDecksRepository {

    override suspend fun save(deck: Deck): Result<Unit> {
        roomDBApi.markFavorite(deck, favorite = true)
        return Result.Success(Unit)
    }

    override suspend fun remove(deck: Deck): Result<Unit> {
        roomDBApi.markFavorite(deck, favorite = false)
        return Result.Success(Unit)
    }

    override suspend fun getPagesQuantity(): Result<Int> {
        val pages = roomDBApi.getPagesQuantity(onlyFavorites = true)
        return Result.Success(pages)
    }

    override suspend fun getPage(pageNumber: Int): Result<Page> {
        val page = roomDBApi.getPage(pageNumber, onlyFavorites = true)
        return Result.Success(page)
    }

    override suspend fun getDeck(deckPreview: DeckPreview): Result<Deck> {
        return roomDBApi.getDeck(deckPreview)?.let { Result.Success(it) }
            ?: Result.Error(NullPointerException())
    }

    override suspend fun isSaved(deckPreview: DeckPreview): Result<Boolean> {
        val isFavorite = roomDBApi.isFavorite(deckPreview)
        return Result.Success(isFavorite)
    }
}