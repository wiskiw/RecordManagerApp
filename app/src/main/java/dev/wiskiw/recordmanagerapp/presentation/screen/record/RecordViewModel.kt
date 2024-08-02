package dev.wiskiw.recordmanagerapp.presentation.screen.record

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

class RecordViewModel(
    savedStateHandle: SavedStateHandle?,
    private val logger: AppLogger,
    private val recordUseCase: RecordUseCase,
) : MviViewModel<RecordUiState, RecordViewModel.Action, RecordViewModel.SideEffect>(savedStateHandle) {

    sealed interface Action : MviAction {
        data object OnRetryClick : Action
    }

    sealed interface SideEffect : MviSideEffect

    override fun createInitialUiState(): RecordUiState = RecordUiState(
        recordId = "",
        recordWithRelations = null,
        isLoading = true,
        error = null,
    )

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnRetryClick -> {
                if (uiStateFlow.value.recordId.isNotBlank()) {
                    fetchRecordWithRelations(uiStateFlow.value.recordId)
                } else {
                    logger.logError("Unexpected state, launchId is empty")
                }
            }
        }
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
            fetchRecordWithRelations(recordId)
        }
    }

    private fun fetchRecordWithRelations(recordId: String) {
        updateState {
            copy(
                isLoading = true,
                error = null,
                recordWithRelations = null,
            )
        }

        viewModelScope.launch {
            recordUseCase.getWithRelations(id = recordId)
                .catch { exception ->
                    updateState {
                        copy(
                            recordWithRelations = null,
                            isLoading = false,
                            error = exception.message,
                        )
                    }
                }
                .collectLatest { recordWithRelations ->
                    updateState {
                        copy(
                            recordWithRelations = recordWithRelations,
                            isLoading = false,
                            error = null,
                        )
                    }
                }
        }
    }
}
