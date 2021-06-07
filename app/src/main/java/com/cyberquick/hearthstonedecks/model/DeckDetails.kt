package com.cyberquick.hearthstonedecks.model

data class DeckDetails(
    val description: String = "",
    val code: String = "",
    val listOfCards: List<Card> = emptyList()
)