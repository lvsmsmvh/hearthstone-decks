package com.cyberquick.hearthstonedecks.data.server.battlenet.hearthstone

import com.cyberquick.hearthstonedecks.data.server.battlenet.entities.GameClassEntity
import com.cyberquick.hearthstonedecks.domain.entities.Card

data class DeckResponse(
    val deckCode: String,
    val version: Long,
    val format: String,
    val `class`: GameClassEntity,
    val cards: List<Card>,
)