package com.cyberquick.hearthstonedecks.domain.entities

data class Card(
    val id: Int,
    val artistName: String,
    val manaCost: Int,
    val image: String,
    val flavorText: String,
)