package com.cyberquick.hearthstonedecks.domain.repositories

import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.*

interface BaseDecksRepository {
    suspend fun getDeck(deckPreview: DeckPreview): Result<Deck>
}