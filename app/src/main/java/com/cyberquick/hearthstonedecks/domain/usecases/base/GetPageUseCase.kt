package com.cyberquick.hearthstonedecks.domain.usecases.base

import com.cyberquick.hearthstonedecks.domain.repositories.DecksRepository

open class GetPageUseCase (private val decksRepository: DecksRepository) {
    suspend operator fun invoke(pageNumber: Int) = decksRepository.getPage(pageNumber)
}