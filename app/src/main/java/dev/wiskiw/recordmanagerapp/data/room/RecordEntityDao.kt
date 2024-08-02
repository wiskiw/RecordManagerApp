package dev.wiskiw.recordmanagerapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.wiskiw.recordmanagerapp.data.room.model.RecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordEntityDao {
    @Query("SELECT * FROM recordentity")
    fun getAll(): Flow<List<RecordEntity>>

    @Query("SELECT * FROM recordentity WHERE id = :id")
    fun get(id: Long): Flow<RecordEntity>

    @Insert
    suspend fun insert(record: RecordEntity): Long

    @Update
    suspend fun update(record: RecordEntity)

    @Query("DELETE FROM recordentity WHERE id IN (:ids)")
    suspend fun delete(vararg ids: Long): Int
}
