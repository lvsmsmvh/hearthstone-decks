package com.cyberquick.hearthstonedecks.domain.repositories

import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.domain.entities.GetPageFilter
import com.cyberquick.hearthstonedecks.domain.entities.Hero
import com.cyberquick.hearthstonedecks.domain.entities.Page

interface FavoriteDecksRepository: BaseDecksRepository {
    suspend fun getPage(pageNumber: Int, filter: GetPageFilter): Result<Page>

    suspend fun save(deck: Deck, cards: List<Card>): Result<Unit>
    suspend fun remove(deckPreview: DeckPreview): Result<Unit>
    suspend fun isSaved(deckPreview: DeckPreview): Result<Boolean>
}