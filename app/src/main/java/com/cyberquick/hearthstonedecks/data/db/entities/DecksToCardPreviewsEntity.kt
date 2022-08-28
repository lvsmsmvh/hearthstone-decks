package com.cyberquick.hearthstonedecks.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

//@Entity(
//    tableName = "deck_ids_with_card_preview_names",
//    foreignKeys = [
//        ForeignKey(
//            entity = DeckEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["deck_id"],
//            onDelete = ForeignKey.CASCADE
//        ),
//        ForeignKey(
//            entity = CardPreviewEntity::class,
//            parentColumns = ["name"],
//            childColumns = ["card_preview_name"],
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
//)
//data class DecksToCardPreviewsEntity(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val deck_id: Long,
//    val card_preview_name: String,
//) {
//    constructor(deckEntityId: Long, cardPreviewName: String) : this(
//        deck_id = deckEntityId,
//        card_preview_name = cardPreviewName,
//    )
//}