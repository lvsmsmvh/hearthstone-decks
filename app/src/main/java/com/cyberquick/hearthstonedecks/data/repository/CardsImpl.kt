package com.cyberquick.hearthstonedecks.data.repository

import com.cyberquick.hearthstonedecks.data.server.blizzard.HearthstoneApiRepository
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.*
import com.cyberquick.hearthstonedecks.domain.repositories.CardsRepository
import javax.inject.Inject

class CardsImpl @Inject constructor(
    private val hearthstoneApiRepository: HearthstoneApiRepository,
) : CardsRepository {

    override suspend fun getCards(deck: Deck): Result<List<Card>> {
        return hearthstoneApiRepository.retrieveCards(deck.code)
    }
}