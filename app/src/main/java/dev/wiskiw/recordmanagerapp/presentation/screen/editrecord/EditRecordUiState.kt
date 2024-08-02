package dev.wiskiw.recordmanagerapp.presentation.screen.editrecord

import android.os.Parcelable
import dev.wiskiw.recordmanagerapp.domain.model.Record
import dev.wiskiw.recordmanagerapp.domain.model.RecordType
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class EditRecordUiState(
    val recordId: String?,
    val isLoading: Boolean,
    val error: RetryableError?,

    val recordName: String,
    val recordDescription: String,
    val recordTypeChips: List<UiTypeChip>,
) : Parcelable {

    companion object {
        fun getUiTypeChips(selectedType: RecordType? = null): List<UiTypeChip> = RecordType.entries
            .map { recordType ->
                UiTypeChip(
                    label = recordType.name,
                    isSelected = selectedType == recordType,
                    recordType = recordType,
                )
            }
    }

    val isSaveAvailable: Boolean
        get() = !isLoading
                && error == null
                && recordName.isNotBlank() // primitive validation

    val isCreateRecordMode: Boolean
        get() = recordId == null

    fun getRecord(): Record {
        return Record(
            id = recordId ?: "",
            name = recordName,
            description = recordDescription,
            type = recordTypeChips.firstOrNull { it.isSelected }?.recordType ?: RecordType.entries.first()
        )
    }

    @Parcelize
    data class UiTypeChip(
        val label: String,
        val isSelected: Boolean,
        val recordType: RecordType,
    ) : Parcelable

    @Parcelize
    data class RetryableError(
        val message: String?,
        val retryAction: @RawValue EditRecordViewModel.Action,
    ) : Parcelable

}
