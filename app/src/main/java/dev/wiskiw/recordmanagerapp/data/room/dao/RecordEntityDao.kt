package dev.wiskiw.recordmanagerapp.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import dev.wiskiw.recordmanagerapp.data.room.dto.RecordEntity
import dev.wiskiw.recordmanagerapp.data.room.dto.RecordRelationCrossRef
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

    @Query(
        """
        DELETE FROM record_relations 
        WHERE 
            (record_id = :firstRecordId AND related_record_id = :secondRecordId) 
            OR 
            (record_id = :secondRecordId AND related_record_id = :firstRecordId) 
        """
    )
    suspend fun deleteRecordRelations(firstRecordId: Long, secondRecordId: Long)

    @Query(
        """
        SELECT * FROM records 
        WHERE id IN (
            SELECT record_id FROM record_relations WHERE related_record_id = :id
            UNION
            SELECT related_record_id FROM record_relations WHERE record_id = :id
        )
        """
    )
    fun getRelatedRecords(id: Long): Flow<List<RecordEntity>>
}
