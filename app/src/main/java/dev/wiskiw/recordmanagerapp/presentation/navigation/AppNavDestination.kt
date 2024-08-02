package dev.wiskiw.recordmanagerapp.presentation.navigation

import kotlinx.serialization.Serializable

object AppNavDestination {

    @Serializable
    data object RecordList

    @Serializable
    data class EditRecord(val id: String?)

    @Serializable
    data class Record(val id: String)
}
