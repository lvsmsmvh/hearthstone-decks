package com.cyberquick.hearthstonedecks.domain.usecases.favorite

import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.domain.repositories.FavoriteDecksRepository
import com.cyberquick.hearthstonedecks.domain.repositories.OnlineDecksRepository
import javax.inject.Inject
import com.cyberquick.hearthstonedecks.domain.common.Result

class AddDeckPreviewToFavoriteUseCase @Inject constructor(
    private val onlineDecksRepository: OnlineDecksRepository,
    private val favoriteDecksRepository: FavoriteDecksRepository,
) {
    suspend operator fun invoke(deckPreview: DeckPreview): Result<Unit> {
        return when (val deckResult = onlineDecksRepository.getDeck(deckPreview)) {
            is Result.Success -> {
                when (val cardsResult = onlineDecksRepository.getCards(deckResult.data)) {
                    is Result.Success -> {
                        favoriteDecksRepository.save(deckResult.data, cardsResult.data)
                    }
                    is Result.Error -> cardsResult
                }
            }
            is Result.Error -> deckResult
        }
    }
}