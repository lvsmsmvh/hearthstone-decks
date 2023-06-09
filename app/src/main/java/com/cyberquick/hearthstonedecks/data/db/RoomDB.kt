package com.cyberquick.hearthstonedecks.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.cyberquick.hearthstonedecks.data.db.dao.DeckDao
import com.cyberquick.hearthstonedecks.data.db.entities.DeckEntity

const val DATABASE_NAME = "room_local_db"

@Database(
    version = 2,
    entities = [
        DeckEntity::class,
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)

abstract class RoomDB : RoomDatabase() {
    abstract fun deckDao(): DeckDao
}