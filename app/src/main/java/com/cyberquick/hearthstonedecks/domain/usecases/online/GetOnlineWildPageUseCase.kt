package com.cyberquick.hearthstonedecks.domain.usecases.online

import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.GameFormat
import com.cyberquick.hearthstonedecks.domain.entities.Hero
import com.cyberquick.hearthstonedecks.domain.entities.Page
import com.cyberquick.hearthstonedecks.domain.repositories.OnlineDecksRepository
import com.cyberquick.hearthstonedecks.domain.usecases.base.GetPageUseCase
import javax.inject.Inject

class GetOnlineWildPageUseCase @Inject constructor(
    private val decksRepository: OnlineDecksRepository
) : GetPageUseCase {
    override suspend fun invoke(pageNumber: Int, heroes: Set<Hero>): Result<Page> {
        return decksRepository.getPage(pageNumber, GameFormat.Wild, heroes)
    }
}