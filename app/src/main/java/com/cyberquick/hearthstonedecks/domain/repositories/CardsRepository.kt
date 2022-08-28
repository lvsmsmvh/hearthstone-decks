package com.cyberquick.hearthstonedecks.domain.repositories

import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.domain.entities.Deck

interface CardsRepository {
    suspend fun getCards(deck: Deck): Result<List<Card>>
}