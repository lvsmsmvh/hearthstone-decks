package com.cyberquick.hearthstonedecks.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_decks",
    foreignKeys = [
        ForeignKey(
            entity = DeckEntity::class,
            parentColumns = ["id"],
            childColumns = ["deck_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class FavoriteDecksEntity(
    @PrimaryKey(autoGenerate = true) val id_fav: Int = 0,
    val deck_id: Int,
) {
    constructor(deckEntityId: Int) : this(
        deck_id = deckEntityId,
    )
}