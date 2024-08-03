package dev.wiskiw.recordmanagerapp.data.repository

import dev.wiskiw.recordmanagerapp.data.DataMapper
import dev.wiskiw.recordmanagerapp.data.DataMapper.Companion.mapAllToDomain
import dev.wiskiw.recordmanagerapp.data.room.dao.RecordEntityDao
import dev.wiskiw.recordmanagerapp.data.room.dto.RecordEntity
import dev.wiskiw.recordmanagerapp.data.room.dto.RecordRelationCrossRef
import dev.wiskiw.recordmanagerapp.domain.model.Record
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRelationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class RoomRecordRelationsRepository(
    private val recordEntityDao: RecordEntityDao,
    private val recordIdMapper: DataMapper<String, Long>,
    private val recordMapper: DataMapper<Record, RecordEntity>,
) : RecordRelationsRepository {

    override fun getRelatedRecords(id: String): Flow<List<Record>> {
        return recordEntityDao.getRelatedRecords(id = recordIdMapper.mapToDto(id))
            .map { relatedRecordEntities -> recordMapper.mapAllToDomain(relatedRecordEntities) }
    }

    override fun addRelation(id: String, relatedId: String): Flow<Unit> {
        val crossRef = RecordRelationCrossRef(
            recordId = recordIdMapper.mapToDto(id),
            relatedRecordId = recordIdMapper.mapToDto(relatedId),
        )

        return flow {
            recordEntityDao.insertRecordRelationCrossRef(crossRef)
            emit(Unit)
        }
    }

    override fun removeRelation(id: String, relatedId: String): Flow<Unit> {
        return flow {
            recordEntityDao.deleteRecordRelations(
                firstRecordId = recordIdMapper.mapToDto(id),
                secondRecordId = recordIdMapper.mapToDto(relatedId),
            )
            emit(Unit)
        }
    }
}
