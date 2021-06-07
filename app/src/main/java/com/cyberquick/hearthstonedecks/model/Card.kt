package com.cyberquick.hearthstonedecks.model

import com.cyberquick.hearthstonedecks.model.enums.CardRarity

data class Card(
    val name: String = "",
    val amount: String = "",
    val rarity: CardRarity = CardRarity.COMMON,
    val cost: String = "",
    val linkOnCard: String = ""
)