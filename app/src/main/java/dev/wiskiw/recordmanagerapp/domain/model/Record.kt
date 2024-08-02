package dev.wiskiw.recordmanagerapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Record(
    val id: String,
    val type: RecordType,
    val name: String,
    val description: String,
) : Parcelable
