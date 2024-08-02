package dev.wiskiw.recordmanagerapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecordWithRelations(
    val record: Record,
    val relations: List<Record>,
) : Parcelable
