package dev.wiskiw.recordmanagerapp.domain.usecase

import dev.wiskiw.recordmanagerapp.domain.model.Record
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow

class SearchRecordUseCase(
    private val recordRepository: RecordRepository,
) {

    fun getAll(searchQuery: String? = null): Flow<List<Record>> {
        return recordRepository.getAll(searchQuery)
    }
}
