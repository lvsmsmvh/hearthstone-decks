package com.cyberquick.hearthstonedecks.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decks")
data class DeckEntity(
    @PrimaryKey val id: Int,
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