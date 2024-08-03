package dev.wiskiw.recordmanagerapp.presentation.screen.record

import android.os.Parcelable
import dev.wiskiw.recordmanagerapp.domain.model.RecordWithRelations
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecordUiState(
    val recordId: String,
    val isLoading: Boolean,
    val error: String?,
    val isRelationsPickerExposed: Boolean,

    val recordWithRelations: RecordWithRelations?,
) : Parcelable
