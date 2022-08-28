package com.cyberquick.hearthstonedecks.domain.entities

data class DeckPreview(
    val id: Int,
    val title: String,
    val gameClass: String,
    val dust: String,
    val timeCreated: String,
    val deckUrl: String,
    val gameFormat: String,
    val views: Int,
    val author: String,
    val rating: String,
    val deckType: String,
)