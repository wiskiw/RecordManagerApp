package dev.wiskiw.recordmanagerapp.presentation.screen.recordlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dev.wiskiw.recordmanagerapp.app.logger.AppLogger
import dev.wiskiw.recordmanagerapp.domain.usecase.RecordUseCase
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviAction
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviSideEffect
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecordListViewModel(
    savedStateHandle: SavedStateHandle?,
    private val logger: AppLogger,
    private val recordUseCase: RecordUseCase,
) : MviViewModel<RecordListUiState, RecordListViewModel.Action, RecordListViewModel.SideEffect>(savedStateHandle) {

    sealed interface Action : MviAction {
        data object OnRetryClick : Action
        data object OnAddClick : Action
        data class OnSearchInput(val value: String) : Action
        data class OnRecordClick(val id: String) : Action
        data class OnEditClick(val id: String) : Action
        data class OnDeleteClick(val id: String) : Action
    }

    sealed interface SideEffect : MviSideEffect {
        data object NavigateToCreateRecordScreen : SideEffect
        data class NavigateToEditRecordScreen(val id: String) : SideEffect
        data class NavigateToRecord(val id: String) : SideEffect
    }

    init {
        fetchRecords()
    }

    override fun createInitialUiState(): RecordListUiState = RecordListUiState(
        searchQuery = "",
        records = emptyList(),
        isLoading = true,
        error = null,
    )

    override fun handleAction(action: Action) {
        when (action) {
            is Action.OnRetryClick -> fetchRecords()
            is Action.OnAddClick -> sendSideEffect(SideEffect.NavigateToCreateRecordScreen)
            is Action.OnSearchInput -> updateState { copy(searchQuery = action.value) }
            is Action.OnRecordClick -> sendSideEffect(SideEffect.NavigateToRecord(id = action.id))
            is Action.OnEditClick -> sendSideEffect(SideEffect.NavigateToEditRecordScreen(id = action.id))
            is Action.OnDeleteClick -> deleteRecord(action.id)
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
            recordUseCase.getAll()
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

    private fun deleteRecord(recordId: String) {
        viewModelScope.launch {
            recordUseCase.delete(recordId).catch { logger.logError("Failed to delete record id:$recordId", it) }.collectLatest {}
        }
    }
}
