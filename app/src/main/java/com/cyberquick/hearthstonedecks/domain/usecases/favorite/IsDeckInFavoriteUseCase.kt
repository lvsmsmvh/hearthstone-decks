package com.cyberquick.hearthstonedecks.domain.usecases.favorite

import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.domain.repositories.FavoriteDecksRepository
import javax.inject.Inject

class IsDeckInFavoriteUseCase @Inject constructor(
    private val decksRepository: FavoriteDecksRepository
) {
    suspend operator fun invoke(deckPreview: DeckPreview) =
        decksRepository.isSaved(deckPreview)
}