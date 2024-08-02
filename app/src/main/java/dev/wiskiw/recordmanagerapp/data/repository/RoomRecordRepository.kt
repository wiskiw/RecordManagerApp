package dev.wiskiw.recordmanagerapp.data.repository

import dev.wiskiw.recordmanagerapp.data.DataMapper
import dev.wiskiw.recordmanagerapp.data.DataMapper.Companion.mapAllToDomain
import dev.wiskiw.recordmanagerapp.data.room.RecordEntityDao
import dev.wiskiw.recordmanagerapp.data.room.model.RecordEntity
import dev.wiskiw.recordmanagerapp.domain.model.Record
import dev.wiskiw.recordmanagerapp.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class RoomRecordRepository(
    private val recordEntityDao: RecordEntityDao,
    private val recordIdMapper: DataMapper<String, Long>,
    private val recordMapper: DataMapper<Record, RecordEntity>,
) : RecordRepository {

    override fun insert(record: Record): Flow<Record> = flow {
        val newEntity = recordMapper.mapToDto(record)
        val id = recordEntityDao.insert(newEntity)

        recordEntityDao.get(id)
            .map { entity -> recordMapper.mapToDomain(entity) }
            .collect(this)
    }

    override fun update(record: Record): Flow<Unit> = flow {
        val entity = recordMapper.mapToDto(record)
        recordEntityDao.update(entity)
        emit(Unit)
    }

    override fun get(id: String): Flow<Record> {
        return recordEntityDao.get(recordIdMapper.mapToDto(id))
            .map { entity -> recordMapper.mapToDomain(entity) }
    }


    override fun getAll(searchQuery: String?): Flow<List<Record>> {
        return recordEntityDao.getAll()
            .map { recordEntityList -> recordMapper.mapAllToDomain(recordEntityList) }
    }

    override fun delete(id: String): Flow<Unit> = flow {
        recordEntityDao.delete(recordIdMapper.mapToDto(id))
        emit(Unit)
    }
}

