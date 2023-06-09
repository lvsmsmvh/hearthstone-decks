package com.cyberquick.hearthstonedecks.domain.usecases.base

import com.cyberquick.hearthstonedecks.domain.common.Result
import com.cyberquick.hearthstonedecks.domain.entities.GetPageFilter
import com.cyberquick.hearthstonedecks.domain.entities.Page

interface GetPageUseCase {
    suspend operator fun invoke(pageNumber: Int, filter: GetPageFilter): Result<Page>
}