package com.cyberquick.hearthstonedecks.domain.repositories

import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.domain.entities.DecksFilter
import com.cyberquick.hearthstonedecks.domain.entities.Page

interface FavoriteDecksRepository: BaseDecksRepository {
    suspend fun getPage(pageNumber: Int, filter: DecksFilter): Result<Page>

    suspend fun save(deckPreview: DeckPreview): Result<Unit>
    suspend fun remove(deckPreview: DeckPreview): Result<Unit>
    suspend fun isSaved(deckPreview: DeckPreview): Result<Boolean>
}