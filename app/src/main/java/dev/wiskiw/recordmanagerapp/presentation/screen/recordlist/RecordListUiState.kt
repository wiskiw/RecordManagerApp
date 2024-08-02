package dev.wiskiw.recordmanagerapp.presentation.screen.recordlist

import android.os.Parcelable
import dev.wiskiw.recordmanagerapp.domain.model.Record
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecordListUiState(
    val isLoading: Boolean,
    val error: String?,
    val records: List<Record>,
) : Parcelable
