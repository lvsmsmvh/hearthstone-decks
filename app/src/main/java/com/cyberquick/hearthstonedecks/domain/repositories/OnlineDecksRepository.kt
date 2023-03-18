package com.cyberquick.hearthstonedecks.domain.repositories

import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.GameFormat
import com.cyberquick.hearthstonedecks.domain.entities.Hero
import com.cyberquick.hearthstonedecks.domain.entities.Page

interface OnlineDecksRepository: BaseDecksRepository {
    suspend fun getPage(pageNumber: Int, gameFormat: GameFormat, heroes: Set<Hero>): Result<Page>
}