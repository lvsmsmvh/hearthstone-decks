package com.cyberquick.hearthstonedecks.domain.usecases.online

import com.cyberquick.hearthstonedecks.domain.repositories.OnlineDecksRepository
import com.cyberquick.hearthstonedecks.domain.usecases.base.GetPageUseCase
import javax.inject.Inject

class GetOnlinePageUseCase @Inject constructor(
    decksRepository: OnlineDecksRepository
) : GetPageUseCase(decksRepository)