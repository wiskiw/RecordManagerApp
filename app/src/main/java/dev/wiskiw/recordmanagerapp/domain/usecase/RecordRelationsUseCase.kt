package dev.wiskiw.recordmanagerapp.domain.usecase

import dev.wiskiw.recordmanagerapp.domain.model.Record
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRelationsRepository
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecordRelationsUseCase(
    private val recordRepository: RecordRepository,
    private val recordRelationsRepository: RecordRelationsRepository,
) {

    fun getRelatedRecords(id: String): Flow<List<Record>> {
        return recordRelationsRepository.getAll(id)
    }

    fun getAvailableRelations(id: String): Flow<List<Record>> {
        return recordRepository.getAll(null)
            .map { recordList ->
                recordList.filter { record -> record.id != id }
            }
    }

    fun saveRelation(id: String, relatedRecordId: String): Flow<Unit> {
        return recordRelationsRepository.addRelation(id, relatedRecordId)
    }

    fun deleteRelation(id: String, relatedRecordId: String): Flow<Unit> {
        return recordRelationsRepository.removeRelation(id, relatedRecordId)
    }
}
