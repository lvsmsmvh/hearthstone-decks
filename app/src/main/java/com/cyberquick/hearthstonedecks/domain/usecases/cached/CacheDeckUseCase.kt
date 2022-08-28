package com.cyberquick.hearthstonedecks.domain.usecases.cached

import com.cyberquick.hearthstonedecks.domain.entities.Deck
import com.cyberquick.hearthstonedecks.domain.repositories.CacheDecksRepository
import javax.inject.Inject

class CacheDeckUseCase @Inject constructor(
    private val cacheDecksRepository: CacheDecksRepository
) {
    suspend operator fun invoke(deck: Deck) = cacheDecksRepository.save(deck)
}