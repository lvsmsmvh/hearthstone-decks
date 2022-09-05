package com.cyberquick.hearthstonedecks.domain.entities

data class Page(
    val totalPagesAmount: Int,
    val number: Int,
    val deckPreviews: List<DeckPreview>,
)