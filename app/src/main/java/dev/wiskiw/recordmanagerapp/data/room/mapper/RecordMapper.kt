package dev.wiskiw.recordmanagerapp.data.room.mapper

import dev.wiskiw.recordmanagerapp.data.DataMapper
import dev.wiskiw.recordmanagerapp.data.room.dto.RecordEntity
import dev.wiskiw.recordmanagerapp.domain.model.Record

class RecordMapper(
    private val recordIdMapper: DataMapper<String, Long>,
) : DataMapper<Record, RecordEntity> {

    override fun mapToDomain(dtoModel: RecordEntity): Record {
        return Record(
            id = recordIdMapper.mapToDomain(dtoModel.id),
            name = dtoModel.name,
            type = dtoModel.type,
            description = dtoModel.description,
        )
    }

    override fun mapToDto(domainModel: Record): RecordEntity {
        return RecordEntity(
            id = recordIdMapper.mapToDto(domainModel.id),
            name = domainModel.name,
            type = domainModel.type,
            description = domainModel.description,
        )
    }
}
