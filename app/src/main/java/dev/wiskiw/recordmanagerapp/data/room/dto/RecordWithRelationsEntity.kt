package dev.wiskiw.recordmanagerapp.data.room.dto

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class RecordWithRelationsEntity(
    @Embedded val record: RecordEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = RecordRelationCrossRef::class,
            parentColumn = "record_id",
            entityColumn = "related_record_id",
        )
    )
    val relatedRecords: List<RecordEntity>,
)
