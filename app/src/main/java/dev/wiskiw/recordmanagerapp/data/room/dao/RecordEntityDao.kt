package dev.wiskiw.recordmanagerapp.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.wiskiw.recordmanagerapp.data.room.dto.RecordEntity
import dev.wiskiw.recordmanagerapp.data.room.dto.RecordRelationCrossRef
import dev.wiskiw.recordmanagerapp.data.room.dto.RecordWithRelationsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordEntityDao {
    @Query("SELECT * FROM records")
    fun getAll(): Flow<List<RecordEntity>>

    @Query("SELECT * FROM records WHERE id = :id")
    fun get(id: Long): Flow<RecordEntity>

    @Insert
    suspend fun insert(record: RecordEntity): Long

    @Update
    suspend fun update(record: RecordEntity)

    @Query("DELETE FROM records WHERE id IN (:ids)")
    suspend fun delete(vararg ids: Long): Int


    @Insert
    suspend fun insertRecordRelationCrossRef(recordRelationCrossRef: RecordRelationCrossRef): Long

    @Query("DELETE FROM record_relations WHERE record_id = :recordId AND related_record_id = :relatedRecordId")
    suspend fun deleteRecordRelation(recordId: Long, relatedRecordId: Long)

    @Transaction
    @Query("SELECT * FROM records WHERE id = :recordId")
    fun getRecordWithRelations(recordId: Long): Flow<RecordWithRelationsEntity>
}
