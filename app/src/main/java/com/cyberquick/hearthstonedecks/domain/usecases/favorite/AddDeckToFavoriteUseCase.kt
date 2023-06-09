package com.cyberquick.hearthstonedecks.domain.usecases.favorite

import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.domain.repositories.FavoriteDecksRepository
import javax.inject.Inject

class AddDeckToFavoriteUseCase @Inject constructor(
    private val favoriteDecksRepository: FavoriteDecksRepository,
) {
    suspend operator fun invoke(deckPreview: DeckPreview) = favoriteDecksRepository.save(deckPreview)
}