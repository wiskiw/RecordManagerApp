package dev.wiskiw.recordmanagerapp.presentation.screen.record

import android.os.Parcelable
import dev.wiskiw.recordmanagerapp.domain.model.Record
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecordUiState(
    val recordId: String,
    val isLoading: Boolean,
    val error: String?,

    val isRelationsPickerExposed: Boolean,

    val record: Record?,
    val relations: List<Record>,
    val availableRelations: List<Record>,
) : Parcelable
