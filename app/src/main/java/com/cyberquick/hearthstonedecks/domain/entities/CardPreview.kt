package com.cyberquick.hearthstonedecks.domain.entities

data class CardPreview(
    val name: String,
    val amount: String,
    val rarity: String,
    val cost: String,
    val cardUrl: String,
)