package dev.wiskiw.recordmanagerapp.presentation.screen.recordlist

import androidx.lifecycle.SavedStateHandle
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviAction
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviSideEffect
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviViewModel

class RecordListScreenViewModel(savedStateHandle: SavedStateHandle?) :
    MviViewModel<RecordListUiState, RecordListScreenViewModel.Action, RecordListScreenViewModel.SideEffect>(savedStateHandle) {

    sealed interface Action : MviAction {
        data class OnRecordClick(val id: String) : Action
    }

    sealed interface SideEffect : MviSideEffect {
        data class NavigateToRecord(val id: String) : SideEffect
    }

    override fun createInitialUiState(): RecordListUiState = RecordListUiState(records = emptyList())

    override fun handleAction(action: Action) {
        when (action) {
            is Action.OnRecordClick -> sendSideEffect(SideEffect.NavigateToRecord(id = action.id))
        }
    }
}
