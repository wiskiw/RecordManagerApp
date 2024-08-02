package dev.wiskiw.recordmanagerapp.presentation.screen.recordlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecordListUiState(
    val records: List<String>,
) : Parcelable
