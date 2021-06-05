package com.cyberquick.hearthstonedecks.model

data class Deck(
    val description: String,
    val code: String,
    val listOfCards: List<Card>
)