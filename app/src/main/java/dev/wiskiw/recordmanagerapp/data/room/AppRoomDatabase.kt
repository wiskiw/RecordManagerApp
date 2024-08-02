package dev.wiskiw.recordmanagerapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.wiskiw.recordmanagerapp.data.room.model.RecordEntity

@Database(
    entities = [
        RecordEntity::class,
    ],
    version = 1,
)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun recordEntityDao(): RecordEntityDao
}
