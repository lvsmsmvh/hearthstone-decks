package com.cyberquick.hearthstonedecks.domain.usecases.cached

import com.cyberquick.hearthstonedecks.domain.repositories.CacheDecksRepository
import javax.inject.Inject

class ClearCachedDecksUseCase @Inject constructor(
    private val decksRepository: CacheDecksRepository
) {
    suspend operator fun invoke() = decksRepository.clearCache()
}