package com.cyberquick.hearthstonedecks.data.db.dao

import androidx.room.Dao

@Dao
interface CardPreviewDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(cardPreviewEntities: CardPreviewEntity)
//
//    @Insert
//    fun insertDeckToCardPreviews(decksToCardPreviewsEntity: DecksToCardPreviewsEntity)
//
//    @Query(
//        "DELETE FROM card_previews WHERE name NOT IN " +
//                "(SELECT B.deck_id FROM deck_ids_with_card_preview_names AS B)"
//    )
//    fun deleteAllThatDoesNotRelateToDecks()
//
//    @Query(
//        "SELECT * FROM card_previews " +
//                "INNER JOIN deck_ids_with_card_preview_names ON name = card_preview_name " +
//                "WHERE deck_id = :deckId"
//    )
//    fun getCardPreviews(deckId: Long): List<CardPreviewEntity>
}