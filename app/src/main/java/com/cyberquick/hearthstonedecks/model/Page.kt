package com.cyberquick.hearthstonedecks.model

data class Page(
    val pageNumber: Int,
    val listOfNews: List<DeckPreview>
)