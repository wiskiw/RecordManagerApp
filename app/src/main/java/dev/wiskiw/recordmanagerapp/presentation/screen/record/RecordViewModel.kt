package dev.wiskiw.recordmanagerapp.presentation.screen.record

import androidx.lifecycle.SavedStateHandle
import dev.wiskiw.recordmanagerapp.domain.usecase.RecordUseCase
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviAction
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviSideEffect
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviViewModel

class RecordViewModel(
    savedStateHandle: SavedStateHandle?,
    private val recordUseCase: RecordUseCase,
) : MviViewModel<RecordUiState, RecordViewModel.Action, RecordViewModel.SideEffect>(savedStateHandle) {

    sealed interface Action : MviAction

    sealed interface SideEffect : MviSideEffect

    override fun createInitialUiState(): RecordUiState = RecordUiState(recordId = "")

    override fun handleAction(action: Action) {
        // nothing to handle yet
    }

    fun onArgsReceived(
        recordId: String,
    ) {
        // fetch data only if recordId has changed
        if (uiStateFlow.value.recordId != recordId) {
            updateState {
                copy(
                    recordId = recordId,
                )
            }
            fetchRecord(recordId)
        }
    }

    private fun fetchRecord(recordId: String) {
        // TODO
        recordUseCase.get(recordId)
    }
}
