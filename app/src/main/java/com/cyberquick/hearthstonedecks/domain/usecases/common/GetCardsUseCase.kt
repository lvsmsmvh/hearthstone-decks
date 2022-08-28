package com.cyberquick.hearthstonedecks.domain.usecases.common

import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.domain.repositories.CardsRepository
import javax.inject.Inject

class GetCardsUseCase @Inject constructor(
    private val cardsRepository: CardsRepository,
) {
    suspend operator fun invoke(deck: Deck): Result<List<Card>> {
        return cardsRepository.getCards(deck)
    }
}