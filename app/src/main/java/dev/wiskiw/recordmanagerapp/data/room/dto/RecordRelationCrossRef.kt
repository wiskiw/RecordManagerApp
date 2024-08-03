package dev.wiskiw.recordmanagerapp.data.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = [
        "record_id",
        "related_record_id",
    ],
    tableName = "record_relations"
)
data class RecordRelationCrossRef(
    @ColumnInfo(name = "record_id") val recordId: Long,
    @ColumnInfo(name = "related_record_id")val relatedRecordId: Long,
)
