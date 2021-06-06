package com.cyberquick.hearthstonedecks.model

import com.cyberquick.hearthstonedecks.model.enums.CardRarity

class Card(
    val name: String,
    val amount: String,
    val rarity: CardRarity,
    val cost: String,
    val linkOnCard: String
)