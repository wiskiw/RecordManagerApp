package dev.wiskiw.recordmanagerapp.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.wiskiw.recordmanagerapp.domain.model.RecordType

@Entity
data class RecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: RecordType,
    @ColumnInfo(name = "description") val description: String,
)
