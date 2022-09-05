package com.cyberquick.hearthstonedecks.domain.entities

data class Deck(
    val deckPreview: DeckPreview,
    val description: String,
    val code: String,
    val cards: List<Card>,
)