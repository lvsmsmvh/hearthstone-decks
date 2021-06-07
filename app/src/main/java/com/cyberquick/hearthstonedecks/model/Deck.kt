package com.cyberquick.hearthstonedecks.model

data class Deck(
    val deckPreview: DeckPreview,
    val description: String,
    val code: String,
    val listOfCards: List<Card>
)