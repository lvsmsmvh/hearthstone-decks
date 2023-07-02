package com.cyberquick.hearthstonedecks.domain.repositories

import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.domain.entities.GameFormat
import com.cyberquick.hearthstonedecks.domain.entities.DecksFilter
import com.cyberquick.hearthstonedecks.domain.entities.Page

interface OnlineDecksRepository : BaseDecksRepository {

    suspend fun getDeck(deckPreview: DeckPreview): Result<Deck>

    suspend fun getPage(
        pageNumber: Int,
        gameFormat: GameFormat,
        filter: DecksFilter
    ): Result<Page>
}