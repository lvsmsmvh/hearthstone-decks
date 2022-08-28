package com.cyberquick.hearthstonedecks.domain.usecases.favorite

import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.repositories.FavoriteDecksRepository
import javax.inject.Inject

class AddDeckToFavoriteUseCase @Inject constructor(
    private val decksRepository: FavoriteDecksRepository
) {
    suspend operator fun invoke(deck: Deck) = decksRepository.save(deck)
}