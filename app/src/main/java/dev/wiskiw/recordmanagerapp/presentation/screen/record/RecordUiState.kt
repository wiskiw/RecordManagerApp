package dev.wiskiw.recordmanagerapp.presentation.screen.record

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecordUiState(
    val recordId: String,
) : Parcelable
