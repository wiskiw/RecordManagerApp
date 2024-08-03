package dev.wiskiw.recordmanagerapp.domain.repository

import dev.wiskiw.recordmanagerapp.domain.model.Record
import kotlinx.coroutines.flow.Flow

interface RecordRelationsRepository {

    fun getRelatedRecords(id: String): Flow<List<Record>>

    fun addRelation(id: String, relatedId: String): Flow<Unit>

    fun removeRelation(id: String, relatedId: String): Flow<Unit>
}
