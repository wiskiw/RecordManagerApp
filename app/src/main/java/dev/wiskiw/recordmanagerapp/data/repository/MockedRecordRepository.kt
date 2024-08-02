package dev.wiskiw.recordmanagerapp.data.repository

import dev.wiskiw.recordmanagerapp.domain.model.Record
import dev.wiskiw.recordmanagerapp.domain.model.RecordType
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

class MockedRecordRepository : RecordRepository {

    companion object {
        private val recordList = listOf(
            getNewRecord("1"),
            getNewRecord("2"),
            getNewRecord("-foo"),
            getNewRecord("-boo"),
        )

        private fun getNewRecord(postfix: String): Record {
            return Record(
                id = "r$postfix",
                type = RecordType.Desk,
                name = "name$postfix",
                description = "description$postfix",
            )
        }
    }

    override fun insert(record: Record): Flow<Record> {
        TODO("Not yet implemented")
    }

    override fun update(record: Record): Flow<Nothing> {
        TODO("Not yet implemented")
    }

    override fun get(id: String): Flow<Record> {
        TODO("Not yet implemented")
    }

    override fun getAll(searchQuery: String?): Flow<List<Record>> {
        return flow {
            delay(1.seconds.inWholeMilliseconds)
            emit(recordList)
        }
    }

    override fun delete(id: String): Flow<Nothing> {
        TODO("Not yet implemented")
    }

}
