package dev.wiskiw.recordmanagerapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.wiskiw.recordmanagerapp.data.room.dao.RecordEntityDao
import dev.wiskiw.recordmanagerapp.data.room.dto.RecordEntity
import dev.wiskiw.recordmanagerapp.data.room.dto.RecordRelationCrossRef

@Database(
    entities = [
        RecordEntity::class,
        RecordRelationCrossRef::class,
    ],
    version = 1,
)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun recordEntityDao(): RecordEntityDao
}
