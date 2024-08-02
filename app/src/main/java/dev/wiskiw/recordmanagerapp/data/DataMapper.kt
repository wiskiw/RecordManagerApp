package dev.wiskiw.recordmanagerapp.data

import dev.wiskiw.recordmanagerapp.data.model.MapperException

interface DataMapper<DomainModel, DtoModel> {

    companion object {
        fun <DomainModel, DtoModel> DataMapper<DomainModel, DtoModel>.mapAllToDomain(dtoModelList: List<DtoModel>) =
            dtoModelList.map(::mapToDomain)


        fun <DomainModel, DtoModel> DataMapper<DomainModel, DtoModel>.mapAllToDto(domainModelList: List<DomainModel>) =
            domainModelList.map(::mapToDto)
    }

    @Throws(MapperException::class)
    fun mapToDomain(dtoModel: DtoModel): DomainModel

    @Throws(MapperException::class)
    fun mapToDto(domainModel: DomainModel): DtoModel
}
