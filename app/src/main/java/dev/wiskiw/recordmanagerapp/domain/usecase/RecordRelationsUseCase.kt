package dev.wiskiw.recordmanagerapp.domain.usecase

import dev.wiskiw.recordmanagerapp.domain.model.Record
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRelationsRepository
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class RecordRelationsUseCase(
    private val recordRepository: RecordRepository,
    private val recordRelationsRepository: RecordRelationsRepository,
) {

    fun getRelatedRecords(id: String): Flow<List<Record>> {
        return recordRelationsRepository.getRelatedRecords(id)
    }

    fun getAvailableRelations(id: String): Flow<List<Record>> {
        return combine(
            recordRelationsRepository.getRelatedRecords(id),
            recordRepository.getAll(),
        ) { relatedRecords, allRecords ->
            allRecords
                .filterNot { record -> record.id == id }
                .filterNot { record -> relatedRecords.contains(record) }
        }
    }

    fun saveRelation(id: String, relatedRecordId: String): Flow<Unit> {
        return recordRelationsRepository.addRelation(id, relatedRecordId)
    }

    fun deleteRelation(id: String, relatedRecordId: String): Flow<Unit> {
        return recordRelationsRepository.removeRelation(id, relatedRecordId)
    }
}
