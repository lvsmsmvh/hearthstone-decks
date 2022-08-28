package com.cyberquick.hearthstonedecks.data.repository

import com.cyberquick.hearthstonedecks.data.db.RoomDBApi
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.*
import com.cyberquick.hearthstonedecks.domain.repositories.CacheDecksRepository
import java.lang.NullPointerException
import javax.inject.Inject

class CacheDecksImpl @Inject constructor(
    private val roomDBApi: RoomDBApi,
) : CacheDecksRepository {

    override suspend fun save(deck: Deck): Result<Unit> {
        roomDBApi.insert(deck)
        return Result.Success(Unit)
    }

    override suspend fun remove(deck: Deck): Result<Unit> {
        roomDBApi.remove(deck)
        return Result.Success(Unit)
    }

    override suspend fun getPagesQuantity(): Result<Int> {
        val pages = roomDBApi.getPagesQuantity(onlyFavorites = false)
        return Result.Success(pages)
    }

    override suspend fun getPage(pageNumber: Int): Result<Page> {
        val page = roomDBApi.getPage(pageNumber, onlyFavorites = false)
        return Result.Success(page)
    }

    override suspend fun getDeck(deckPreview: DeckPreview): Result<Deck> {
        return roomDBApi.getDeck(deckPreview)?.let { Result.Success(it) }
            ?: Result.Error(NullPointerException())
    }

    override suspend fun isSaved(deckPreview: DeckPreview): Result<Boolean> {
        val isSaved = roomDBApi.isSaved(deckPreview)
        return Result.Success(isSaved)
    }

    override suspend fun clearCache(): Result<Unit> {
        roomDBApi.clearCache()
        return Result.Success(Unit)
    }
}