package com.cyberquick.hearthstonedecks.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey val id: Int,
    val artistName: String,
    val manaCost: Int,
    val image: String,
    val flavorText: String,
    val cardSetId: Int,
)