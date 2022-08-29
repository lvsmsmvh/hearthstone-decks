package com.cyberquick.hearthstonedecks.domain.usecases.base

import com.cyberquick.hearthstonedecks.domain.repositories.BaseDecksRepository

open class GetPagesQuantityUseCase(private val decksRepository: BaseDecksRepository) {
    suspend operator fun invoke() = decksRepository.getPagesQuantity()
}