package com.cyberquick.hearthstonedecks.domain.usecases.base

import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.Page

interface GetPageUseCase {
    suspend operator fun invoke(pageNumber: Int): Result<Page>
}