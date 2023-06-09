package com.cyberquick.hearthstonedecks.data.db.dao

import androidx.room.*
import com.cyberquick.hearthstonedecks.data.db.entities.DeckEntity

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

    @Delete
    fun delete(deckEntity: DeckEntity?)
}