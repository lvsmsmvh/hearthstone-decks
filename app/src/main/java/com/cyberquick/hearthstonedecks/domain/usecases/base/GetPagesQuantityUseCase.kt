package com.cyberquick.hearthstonedecks.domain.usecases.base

import com.cyberquick.hearthstonedecks.domain.repositories.DecksRepository

open class GetPagesQuantityUseCase(private val decksRepository: DecksRepository) {
    suspend operator fun invoke() = decksRepository.getPagesQuantity()
}