package com.cyberquick.hearthstonedecks.domain.usecases.favorite

import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.DecksFilter
import com.cyberquick.hearthstonedecks.domain.entities.Page
import com.cyberquick.hearthstonedecks.domain.repositories.FavoriteDecksRepository
import com.cyberquick.hearthstonedecks.domain.usecases.base.GetPageUseCase
import javax.inject.Inject

class GetFavoritePageUseCase @Inject constructor(
    private val decksRepository: FavoriteDecksRepository
) : GetPageUseCase {
    override suspend fun invoke(pageNumber: Int, filter: DecksFilter): Result<Page> {
        return decksRepository.getPage(pageNumber, filter)
    }
}