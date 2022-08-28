package com.cyberquick.hearthstonedecks.domain.usecases.common

import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.entities.DeckPreview
import com.cyberquick.hearthstonedecks.domain.repositories.OnlineDecksRepository
import com.cyberquick.hearthstonedecks.domain.repositories.CacheDecksRepository
import com.cyberquick.hearthstonedecks.domain.common.Result
import javax.inject.Inject

class GetDeckUseCase @Inject constructor(
    private val onlineDecksRepository: OnlineDecksRepository,
    private val cacheDecksRepository: CacheDecksRepository,
) {
    suspend operator fun invoke(deckPreview: DeckPreview): Result<Deck> {
        cacheDecksRepository.getDeck(deckPreview).let { result ->
            if (result is Result.Success) return result
        }
        return onlineDecksRepository.getDeck(deckPreview)
    }
}