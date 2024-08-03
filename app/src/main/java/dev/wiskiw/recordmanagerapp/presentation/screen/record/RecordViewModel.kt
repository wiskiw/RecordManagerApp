package dev.wiskiw.recordmanagerapp.presentation.screen.record

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dev.wiskiw.recordmanagerapp.app.logger.AppLogger
import dev.wiskiw.recordmanagerapp.domain.usecase.RecordRelationsUseCase
import dev.wiskiw.recordmanagerapp.domain.usecase.RecordUseCase
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviAction
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviSideEffect
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class RecordViewModel(
    savedStateHandle: SavedStateHandle?,
    private val logger: AppLogger,
    private val recordUseCase: RecordUseCase,
    private val recordRelationsUseCase: RecordRelationsUseCase,
) : MviViewModel<RecordUiState, RecordViewModel.Action, RecordViewModel.SideEffect>(savedStateHandle) {

    sealed interface Action : MviAction {
        data object OnBackClick : Action
        data object OnRetryClick : Action
        data object OnAddRelationClick : Action
        data object OnDialogDismiss : Action
        data class OnRelationSelect(val relatedRecordId: String) : Action
        data class OnRelationDelete(val relatedRecordId: String) : Action
    }

    sealed interface SideEffect : MviSideEffect {
        data object NavigateBack : SideEffect
    }

    override fun createInitialUiState(): RecordUiState = RecordUiState(
        recordId = "",
        record = null,
        relations = emptyList(),
        availableRelations = emptyList(),
        isLoading = true,
        error = null,
        isRelationsPickerExposed = false,
    )

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnBackClick -> sendSideEffect(SideEffect.NavigateBack)

            Action.OnRetryClick -> {
                if (uiStateFlow.value.recordId.isNotBlank()) {
                    fetchRecordWithRelations(uiStateFlow.value.recordId)
                } else {
                    logger.logError("Unexpected state, launchId is empty")
                }
            }

            Action.OnAddRelationClick -> updateState { copy(isRelationsPickerExposed = true) }
            Action.OnDialogDismiss -> updateState { copy(isRelationsPickerExposed = false) }
            is Action.OnRelationSelect -> {
                updateState { copy(isRelationsPickerExposed = false) }
                addRelation(action.relatedRecordId)
            }
            is Action.OnRelationDelete -> removeRelation(action.relatedRecordId)
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
                record = null,
                relations = emptyList(),
                availableRelations = emptyList(),
            )
        }

        viewModelScope.launch {
            combine(
                recordUseCase.get(recordId),
                recordRelationsUseCase.getRelatedRecords(recordId),
                recordRelationsUseCase.getAvailableRelations(recordId),
            ) { record, relations, availableRelation ->
                updateState {
                    copy(
                        record = record,
                        relations = relations,
                        availableRelations = availableRelation,
                        isLoading = false,
                        error = null,
                    )
                }
            }
                .catch { exception ->
                    updateState {
                        copy(
                            record = null,
                            relations = emptyList(),
                            availableRelations = emptyList(),
                            isLoading = false,
                            error = exception.message,
                        )
                    }
                }
                .collect()
        }
    }

    private fun addRelation(relationRecordId: String) {
        val recordId = uiStateFlow.value.record?.id ?: run {
            logger.logError("Unexpected state. Attempted to add related record to null record")
            return
        }

        updateState {
            copy(
                isLoading = true,
                error = null,
            )
        }

        viewModelScope.launch {
            recordRelationsUseCase.saveRelation(recordId, relationRecordId)
                .catch { exception ->
                    updateState {
                        copy(
                            record = null,
                            relations = emptyList(),
                            availableRelations = emptyList(),
                            isLoading = false,
                            error = exception.message,
                        )
                    }
                }
                .collectLatest {
                    updateState {
                        copy(
                            isLoading = false,
                            error = null,
                        )
                    }
                }
        }
    }

    private fun removeRelation(relationRecordId: String) {
        val recordId = uiStateFlow.value.record?.id ?: run {
            logger.logError("Unexpected state. Attempted to remove related record to null record")
            return
        }

        updateState {
            copy(
                isLoading = true,
                error = null,
            )
        }

        viewModelScope.launch {
            recordRelationsUseCase.deleteRelation(recordId, relationRecordId)
                .catch { exception ->
                    updateState {
                        copy(
                            record = null,
                            relations = emptyList(),
                            availableRelations = emptyList(),
                            isLoading = false,
                            error = exception.message,
                        )
                    }
                }
                .collectLatest {
                    updateState {
                        copy(
                            isLoading = false,
                            error = null,
                        )
                    }
                }
        }
    }
}
