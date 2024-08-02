package dev.wiskiw.recordmanagerapp.domain.usecase

import dev.wiskiw.recordmanagerapp.domain.model.Record
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRelationsRepository
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecordUseCase(
    private val recordRepository: RecordRepository,
    private val recordRelationsRepository: RecordRelationsRepository,
) {

    fun insert(record: Record): Flow<Record> {
        return recordRepository.insert(record)
    }

    fun update(record: Record): Flow<Nothing> {
        return recordRepository.update(record)
    }

    fun get(id: String): Flow<Record> {
        return recordRepository.get(id)
    }

    fun delete(id: String): Flow<Nothing> {
        return recordRepository.delete(id)
    }

    fun getRelatedRecords(id: String): Flow<List<Record>> {
        return recordRelationsRepository.getAll(id)
    }
}
