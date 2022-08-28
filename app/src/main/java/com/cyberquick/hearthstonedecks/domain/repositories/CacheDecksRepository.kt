package com.cyberquick.hearthstonedecks.domain.repositories

import com.cyberquick.hearthstonedecks.domain.common.Result

interface CacheDecksRepository: DecksRepository, Savable {
    suspend fun clearCache(): Result<Unit>
}