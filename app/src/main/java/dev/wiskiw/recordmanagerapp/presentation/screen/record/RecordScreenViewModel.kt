package dev.wiskiw.recordmanagerapp.presentation.screen.record

import androidx.lifecycle.SavedStateHandle
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviAction
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviSideEffect
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviViewModel

class RecordScreenViewModel(savedStateHandle: SavedStateHandle?) :
    MviViewModel<RecordUiState, RecordScreenViewModel.Action, RecordScreenViewModel.SideEffect>(savedStateHandle) {

    sealed interface Action : MviAction

    sealed interface SideEffect : MviSideEffect

    override fun createInitialUiState(): RecordUiState = RecordUiState(recordId = "")

    override fun handleAction(action: Action) {
        // nothing to handle yet
    }

    fun onArgsReceived(
        launchId: String,
    ) {
        updateState {
            copy(
                recordId = launchId,
            )
        }
    }
}
