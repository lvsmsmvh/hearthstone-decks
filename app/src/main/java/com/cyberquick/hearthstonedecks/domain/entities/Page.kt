package com.cyberquick.hearthstonedecks.domain.entities

data class Page(
    val number: Int,
    val deckPreviews: List<DeckPreview>,
)