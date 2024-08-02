package dev.wiskiw.recordmanagerapp.presentation.screen.recordlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dev.wiskiw.recordmanagerapp.domain.usecase.SearchRecordUseCase
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviAction
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviSideEffect
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecordListViewModel(
    savedStateHandle: SavedStateHandle?,
    private val searchRecordUseCase: SearchRecordUseCase,
) : MviViewModel<RecordListUiState, RecordListViewModel.Action, RecordListViewModel.SideEffect>(savedStateHandle) {

    sealed interface Action : MviAction {
        data object OnRetryClick : Action
        data class OnRecordClick(val id: String) : Action
    }

    sealed interface SideEffect : MviSideEffect {
        data class NavigateToRecord(val id: String) : SideEffect
    }

    init {
        fetchRecords()
    }

    override fun createInitialUiState(): RecordListUiState = RecordListUiState(
        records = emptyList(),
        isLoading = true,
        error = null,
    )

    override fun handleAction(action: Action) {
        when (action) {
            is Action.OnRetryClick -> fetchRecords()
            is Action.OnRecordClick -> sendSideEffect(SideEffect.NavigateToRecord(id = action.id))
        }
    }

    private fun fetchRecords() {
        updateState {
            copy(
                records = emptyList(),
                isLoading = true,
                error = null,
            )
        }

        viewModelScope.launch {
            searchRecordUseCase.getAll()
                .catch {
                    updateState {
                        copy(
                            records = emptyList(),
                            isLoading = false,
                            error = it.message,
                        )
                    }
                }
                .collectLatest { launches ->
                    updateState {
                        copy(
                            records = launches,
                            isLoading = false,
                            error = null,
                        )
                    }
                }
        }
    }
}
