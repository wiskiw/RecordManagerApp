package dev.wiskiw.recordmanagerapp.domain.usecase

import dev.wiskiw.recordmanagerapp.domain.model.Record
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRelationsRepository
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class RecordUseCase(
    private val recordRepository: RecordRepository,
) {

    fun insert(record: Record): Flow<Record> {
        return recordRepository.insert(record)
    }

    fun update(record: Record): Flow<Unit> {
        return recordRepository.update(record)
    }

    fun get(id: String): Flow<Record> {
        return recordRepository.get(id)
    }

    fun delete(id: String): Flow<Unit> {
        return recordRepository.delete(id)
    }
}
