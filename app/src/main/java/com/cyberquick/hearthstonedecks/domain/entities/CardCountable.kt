package com.cyberquick.hearthstonedecks.domain.entities

data class CardCountable(
    val card: Card,
    val amount: Int = 0,
)