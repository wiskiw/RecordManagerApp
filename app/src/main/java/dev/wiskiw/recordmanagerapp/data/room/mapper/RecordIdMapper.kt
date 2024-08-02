package dev.wiskiw.recordmanagerapp.data.room.mapper

import dev.wiskiw.recordmanagerapp.data.DataMapper
import dev.wiskiw.recordmanagerapp.data.model.DataLayerException

class RecordIdMapper : DataMapper<String, Long> {
    override fun mapToDomain(dtoModel: Long): String = dtoModel.toString()

    override fun mapToDto(domainModel: String): Long {
        try {
            return if (domainModel.isBlank()) {
                0L
            } else {
                domainModel.toLong()
            }
        } catch (nfe: NumberFormatException) {
            throw DataLayerException("Unable to convert record id to long", nfe)
        }
    }
}
