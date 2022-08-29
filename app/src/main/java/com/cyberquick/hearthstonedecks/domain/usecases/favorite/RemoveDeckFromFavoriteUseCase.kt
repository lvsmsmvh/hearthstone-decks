package com.cyberquick.hearthstonedecks.domain.usecases.favorite

import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.domain.repositories.FavoriteDecksRepository
import javax.inject.Inject

class RemoveDeckFromFavoriteUseCase @Inject constructor(
    private val decksRepository: FavoriteDecksRepository
) {
    suspend operator fun invoke(deckPreview: DeckPreview) = decksRepository.remove(deckPreview)
}