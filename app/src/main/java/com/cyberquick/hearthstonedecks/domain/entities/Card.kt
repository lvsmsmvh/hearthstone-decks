package com.cyberquick.hearthstonedecks.domain.entities

data class Card(
    val id: Int,
    val manaCost: Int,
    val image: String,
    val artistName: String?,
    val flavorText: String?,
    val cardSetId: Int,
)