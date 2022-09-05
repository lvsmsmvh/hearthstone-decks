package com.cyberquick.hearthstonedecks.data.db.dao

import androidx.room.*
import com.cyberquick.hearthstonedecks.data.db.entities.CardEntity
import com.cyberquick.hearthstonedecks.data.db.entities.DeckEntity
import com.cyberquick.hearthstonedecks.data.db.entities.DeckToCardEntity

@Dao
interface DeckDao {
    @Query("SELECT COUNT(*) FROM decks")
    fun amountDecks(): Int

    @Query("SELECT * FROM decks LIMIT :from, :to")
    fun getDeckEntities(from: Int, to: Int): List<DeckEntity>

    @Query("SELECT * FROM decks WHERE id = :id")
    fun getDeckEntity(id: Int): DeckEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deckEntities: DeckEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cardEntity: CardEntity)

    @Insert
    fun insert(deckToCardEntity: DeckToCardEntity)

    @Delete
    fun delete(deckEntity: DeckEntity?)

    @Query(
        "SELECT * FROM deck_ids_to_card_ids WHERE deck_id = :deckId"
    )
    fun getCardsForDeckId(deckId: Int): List<DeckToCardEntity>

    @Query(
        "SELECT * FROM cards WHERE id IN (:cardIds)"
    )
    fun getCards(cardIds: List<Int>): List<CardEntity>

    @Query(
        "DELETE FROM cards WHERE id NOT IN " +
                "(SELECT A.card_id FROM deck_ids_to_card_ids AS A)"
    )
    fun deleteCardsThatDoesNotHaveDeck()
}