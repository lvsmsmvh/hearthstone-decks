package com.cyberquick.hearthstonedecks.data.db.dao

import androidx.room.*
import com.cyberquick.hearthstonedecks.data.db.entities.DeckEntity
import com.cyberquick.hearthstonedecks.data.db.entities.FavoriteDecksEntity

@Dao
interface DeckDao {
    @Query("SELECT COUNT(*) FROM decks")
    fun amountAll(): Int

    @Query("SELECT COUNT(*) FROM favorite_decks")
    fun amountFavorites(): Int

    @Query("SELECT * FROM decks LIMIT :from, :to")
    fun getAll(from: Int, to: Int): List<DeckEntity>

    @Query("SELECT * FROM decks INNER JOIN favorite_decks ON id = deck_id LIMIT :from, :to")
    fun getFavorites(from: Int, to: Int): List<DeckEntity>

    @Query("SELECT * FROM decks WHERE id LIKE :id")
    fun getDeckEntity(id: Int): DeckEntity?

    @Query("SELECT * FROM favorite_decks WHERE deck_id LIKE :deckId")
    fun getFavoriteEntity(deckId: Int): FavoriteDecksEntity?

    @Query(
        "DELETE FROM decks WHERE id NOT IN " +
                "(SELECT B.deck_id FROM favorite_decks AS B)"
    )
    fun deleteNotFavorites()

    @Insert
    fun insert(favoriteDecksEntity: FavoriteDecksEntity)

    @Delete
    fun delete(favoriteDecksEntity: FavoriteDecksEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(deckEntities: DeckEntity): Long

    @Delete
    fun delete(deckEntity: DeckEntity?)
}