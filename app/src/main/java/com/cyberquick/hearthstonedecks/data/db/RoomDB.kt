package com.cyberquick.hearthstonedecks.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cyberquick.hearthstonedecks.data.db.dao.CardPreviewDao
import com.cyberquick.hearthstonedecks.data.db.dao.DeckDao
import com.cyberquick.hearthstonedecks.data.db.entities.DeckEntity
import com.cyberquick.hearthstonedecks.data.db.entities.FavoriteDecksEntity

const val DATABASE_NAME = "room_local_db"
private const val CURRENT_DB_VERSION = 4

@Database(
    entities = [
        DeckEntity::class,
        FavoriteDecksEntity::class,
    ],
    version = CURRENT_DB_VERSION
)
abstract class RoomDB : RoomDatabase() {
    abstract fun deckDao(): DeckDao
    abstract fun cardPreviewDao(): CardPreviewDao
}