package com.cyberquick.hearthstonedecks.domain.usecases.common

import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.Card
import com.cyberquick.hearthstonedecks.domain.repositories.FavoriteDecksRepository
import com.cyberquick.hearthstonedecks.domain.repositories.OnlineDecksRepository
import javax.inject.Inject

class GetCardsUseCase @Inject constructor(
    private val onlineDecksRepository: OnlineDecksRepository,
    private val favoriteDecksRepository: FavoriteDecksRepository,
) {
    suspend operator fun invoke(deck: Deck): Result<List<Card>> {
        favoriteDecksRepository.getCards(deck).let { result ->
            if (result is Result.Success) return result
        }
        return onlineDecksRepository.getCards(deck)
    }
}