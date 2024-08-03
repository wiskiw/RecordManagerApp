package dev.wiskiw.recordmanagerapp.data.repository

import dev.wiskiw.recordmanagerapp.domain.model.Record
import dev.wiskiw.recordmanagerapp.domain.model.RecordType
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRelationsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

class MockedRecordRelationsRepository : RecordRelationsRepository {

    companion object {
        private val recordList = listOf(
            getNewRecord("1"),
            getNewRecord("2"),
            getNewRecord("-foo"),
            getNewRecord("-boo"),
        )

        private fun getNewRecord(postfix: String): Record {
            return Record(
                id = "relation $postfix",
                type = RecordType.Desk,
                name = "relation name $postfix",
                description = "relation description$postfix",
            )
        }
    }

    override fun getRelatedRecords(id: String): Flow<List<Record>> {
        return flow {
            delay(1.seconds.inWholeMilliseconds)
            emit(recordList)
        }
    }

    override fun addRelation(id: String, relatedId: String): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override fun removeRelation(id: String, relatedId: String): Flow<Unit> {
        TODO("Not yet implemented")
    }

}
