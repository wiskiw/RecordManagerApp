package dev.wiskiw.recordmanagerapp.domain.model

data class Record(
    val id: String,
    val type: RecordType,
    val name: String,
    val description: String,
)
