package com.cyberquick.hearthstonedecks.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.cyberquick.hearthstonedecks.data.db.dao.DeckDao
import com.cyberquick.hearthstonedecks.data.db.entities.DeckEntity

const val DATABASE_NAME = "room_local_db"

@Database(
    version = 2,
    entities = [
        DeckEntity::class,
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = RoomDB.MyAutoMigrationFrom1To2::class)
    ]
)

abstract class RoomDB : RoomDatabase() {

    abstract fun deckDao(): DeckDao

    @DeleteTable(tableName = "deck_ids_to_card_ids")
    @DeleteTable(tableName = "cards")
    @DeleteColumn(tableName = "cards", columnName = "description")
    @DeleteColumn(tableName = "cards", columnName = "code")
    class MyAutoMigrationFrom1To2 : AutoMigrationSpec
}