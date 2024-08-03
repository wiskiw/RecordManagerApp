package dev.wiskiw.recordmanagerapp.presentation.screen.recordlist

import android.os.Parcelable
import dev.wiskiw.recordmanagerapp.domain.model.Record
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecordListUiState(
    val isLoading: Boolean,
    val error: String?,

    val searchQuery: String,
    val records: List<Record>,
) : Parcelable {

    companion object {
        private fun Record.doesMatchSearchQuery(query: String): Boolean =
            name.contains(query, ignoreCase = true)
                    || description.contains(query, ignoreCase = true)
                    || type.name.contains(query, ignoreCase = true)
    }

    val filteredRecords: List<Record>
        get() {
            return if (searchQuery.isNotBlank()) {
                records.filter { it.doesMatchSearchQuery(searchQuery) }
            } else {
                records
            }
        }
}
