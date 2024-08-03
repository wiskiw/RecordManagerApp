package dev.wiskiw.recordmanagerapp.domain.repository

import dev.wiskiw.recordmanagerapp.domain.model.Record
import kotlinx.coroutines.flow.Flow

interface RecordRepository {

    /**
     * Ignores [record]'s id. Returns record with actual ID.
     */
    fun insert(record: Record): Flow<Record>

    fun update(record: Record): Flow<Unit>

    fun get(id: String): Flow<Record>

    fun getAll(): Flow<List<Record>>

    fun delete(id: String): Flow<Unit>
}
