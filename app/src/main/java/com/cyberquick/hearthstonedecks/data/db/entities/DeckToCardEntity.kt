package com.cyberquick.hearthstonedecks.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "deck_ids_to_card_ids",
    foreignKeys = [
        ForeignKey(
            entity = DeckEntity::class,
            parentColumns = ["id"],
            childColumns = ["deck_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CardEntity::class,
            parentColumns = ["id"],
            childColumns = ["card_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DeckToCardEntity(
    @PrimaryKey(autoGenerate = true) val randomId: Int = 0,
    val deck_id: Int,
    val card_id: Int,
    val copies: Int,
) {
    constructor(deckId: Int, cardId: Int, copies: Int) : this(
        deck_id = deckId,
        card_id = cardId,
        copies = copies,
    )
}