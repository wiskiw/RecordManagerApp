package dev.wiskiw.recordmanagerapp.domain.repository

import dev.wiskiw.recordmanagerapp.domain.model.Record
import kotlinx.coroutines.flow.Flow

interface RecordRelationsRepository {

    fun getAll(id: String): Flow<List<Record>>

    fun insert(id: String, relatedRecordIds: List<String>)

    fun delete(id: String)
}
