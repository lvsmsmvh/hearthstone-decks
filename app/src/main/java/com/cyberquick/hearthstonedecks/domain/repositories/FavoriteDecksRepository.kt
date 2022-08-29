package com.cyberquick.hearthstonedecks.domain.repositories

import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview

interface FavoriteDecksRepository: BaseDecksRepository {
    suspend fun save(deck: Deck, cards: List<Card>): Result<Unit>
    suspend fun remove(deckPreview: DeckPreview): Result<Unit>
    suspend fun isSaved(deckPreview: DeckPreview): Result<Boolean>
}