package com.cyberquick.hearthstonedecks.domain.entities

data class CardCountable(
    val card: Card,
    var amount: Int = 0,
)