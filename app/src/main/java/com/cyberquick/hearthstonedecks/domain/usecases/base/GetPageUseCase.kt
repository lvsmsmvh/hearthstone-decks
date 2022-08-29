package com.cyberquick.hearthstonedecks.domain.usecases.base

import com.cyberquick.hearthstonedecks.domain.repositories.BaseDecksRepository

open class GetPageUseCase (private val decksRepository: BaseDecksRepository) {
    suspend operator fun invoke(pageNumber: Int) = decksRepository.getPage(pageNumber)
}