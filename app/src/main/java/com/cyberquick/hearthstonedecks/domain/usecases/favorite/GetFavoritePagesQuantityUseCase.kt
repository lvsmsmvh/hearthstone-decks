package com.cyberquick.hearthstonedecks.domain.usecases.favorite

import com.cyberquick.hearthstonedecks.domain.repositories.FavoriteDecksRepository
import com.cyberquick.hearthstonedecks.domain.usecases.base.GetPagesQuantityUseCase
import javax.inject.Inject

class GetFavoritePagesQuantityUseCase @Inject constructor(
    decksRepository: FavoriteDecksRepository
) : GetPagesQuantityUseCase(decksRepository)