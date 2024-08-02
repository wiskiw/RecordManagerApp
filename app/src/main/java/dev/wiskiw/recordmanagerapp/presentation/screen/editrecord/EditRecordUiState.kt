package dev.wiskiw.recordmanagerapp.presentation.screen.editrecord

import android.os.Parcelable
import dev.wiskiw.recordmanagerapp.domain.model.RecordType
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditRecordUiState(
    val recordId: String,
    val isLoading: Boolean,
    val error: String?,

    val recordName: String,
    val recordDescription: String,
    val recordTypeChips: List<UiTypeChip>,
) : Parcelable {

    companion object {

        fun getUiTypeChips(): List<UiTypeChip> = RecordType.entries
            .map { recordType ->
                UiTypeChip(
                    label = recordType.name,
                    selected = false,
                    recordType = recordType,
                )
            }
    }

    @Parcelize
    data class UiTypeChip(
        val label: String,
        val selected: Boolean,
        val recordType: RecordType,
    ) : Parcelable

}
