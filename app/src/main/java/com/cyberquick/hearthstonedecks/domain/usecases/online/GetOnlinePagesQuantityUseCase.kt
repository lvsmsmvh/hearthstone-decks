package com.cyberquick.hearthstonedecks.domain.usecases.online

import com.cyberquick.hearthstonedecks.domain.repositories.OnlineDecksRepository
import com.cyberquick.hearthstonedecks.domain.usecases.base.GetPagesQuantityUseCase
import javax.inject.Inject

class GetOnlinePagesQuantityUseCase @Inject constructor(
    decksRepository: OnlineDecksRepository
) : GetPagesQuantityUseCase(decksRepository)