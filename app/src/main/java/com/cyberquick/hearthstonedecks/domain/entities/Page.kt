package com.cyberquick.hearthstonedecks.domain.entities

data class Page(
    val pageNumber: Int,
    val deckPreviews: List<DeckPreview>,
)