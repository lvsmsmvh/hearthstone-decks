package com.cyberquick.hearthstonedecks.domain.repositories

import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview

interface Savable {
    suspend fun save(deck: Deck): Result<Unit>
    suspend fun remove(deck: Deck): Result<Unit>
    suspend fun isSaved(deckPreview: DeckPreview): Result<Boolean>
}