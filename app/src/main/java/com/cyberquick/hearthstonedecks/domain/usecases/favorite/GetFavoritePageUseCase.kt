package com.cyberquick.hearthstonedecks.domain.usecases.favorite

import com.cyberquick.hearthstonedecks.domain.repositories.FavoriteDecksRepository
import com.cyberquick.hearthstonedecks.domain.usecases.base.GetPageUseCase
import javax.inject.Inject

class GetFavoritePageUseCase @Inject constructor(
    decksRepository: FavoriteDecksRepository
) : GetPageUseCase(decksRepository)