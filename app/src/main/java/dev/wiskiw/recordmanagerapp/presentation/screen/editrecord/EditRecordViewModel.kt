package dev.wiskiw.recordmanagerapp.presentation.screen.editrecord

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dev.wiskiw.recordmanagerapp.app.logger.AppLogger
import dev.wiskiw.recordmanagerapp.domain.model.RecordType
import dev.wiskiw.recordmanagerapp.domain.usecase.RecordUseCase
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviAction
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviSideEffect
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Create and edit [Record] screen.
 * 
 * The [EditRecordUiState.recordId] defines the screen mod: edit mode if [EditRecordUiState.recordId] has not null value.
 */
class EditRecordViewModel(
    savedStateHandle: SavedStateHandle?,
    private val logger: AppLogger,
    private val recordUseCase: RecordUseCase,
) : MviViewModel<EditRecordUiState, EditRecordViewModel.Action, EditRecordViewModel.SideEffect>(savedStateHandle) {

    sealed interface Action : MviAction {
        data object OnBackClick : Action
        data object OnSaveClick : Action
        data object OnLoadRecordRetryClick : Action
        data object OnSaveRecordRetryClick : Action
        data class OnNameInput(val value: String) : Action
        data class OnDescriptionInput(val value: String) : Action
        data class OnRecordTypeSelected(val uiTypeChip: EditRecordUiState.UiTypeChip) : Action
    }

    sealed interface SideEffect : MviSideEffect {
        data object NavigateBack : SideEffect
    }

    override fun createInitialUiState(): EditRecordUiState = EditRecordUiState(
        recordId = null,
        isLoading = false,
        error = null,

        recordName = "",
        recordDescription = "",

        // Select first RecordType as default pre-selected value
        recordTypeChips = EditRecordUiState.getUiTypeChips(RecordType.entries.first()),
    )

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnBackClick -> sendSideEffect(SideEffect.NavigateBack)
            Action.OnSaveClick -> saveRecord(closeScreenOnSuccess = true)

            Action.OnLoadRecordRetryClick -> {
                uiStateFlow.value.recordId?.let { recordId -> fetchRecord(recordId) }
                    ?: run { logger.logError("Unexpected state, recordId is null, but retry was clicked") }
            }

            Action.OnSaveRecordRetryClick -> {
                uiStateFlow.value.recordId?.let { recordId -> fetchRecord(recordId) }
                    ?: run { logger.logError("Unexpected state, recordId is null, but retry was clicked") }
            }


            is Action.OnNameInput -> updateState { copy(recordName = action.value) }
            is Action.OnDescriptionInput -> updateState { copy(recordDescription = action.value) }
            is Action.OnRecordTypeSelected -> {
                updateState {
                    val updatedTypeList = recordTypeChips.map {
                        if (it == action.uiTypeChip) {
                            it.copy(isSelected = !it.isSelected)
                        } else {
                            it.copy(isSelected = false)
                        }
                    }
                    copy(recordTypeChips = updatedTypeList)
                }
            }
        }
    }

    fun onArgsReceived(recordId: String?) {
        if (recordId != null && uiStateFlow.value.recordId != recordId) {
            updateState {
                copy(
                    recordId = recordId,
                    isLoading = true,
                )
            }
            fetchRecord(recordId)
        }
    }

    private fun fetchRecord(recordId: String) {
        updateState {
            copy(
                isLoading = true,
                error = null,
            )
        }

        viewModelScope.launch {
            recordUseCase.get(id = recordId).catch { exception ->
                updateState {
                    copy(
                        isLoading = false,
                        error = EditRecordUiState.RetryableError(exception.message, Action.OnLoadRecordRetryClick),
                    )
                }
            }.collectLatest { record ->
                updateState {
                    copy(
                        recordName = record.name,
                        recordDescription = record.description,
                        recordTypeChips = EditRecordUiState.getUiTypeChips(record.type),
                        isLoading = false,
                        error = null,
                    )
                }
            }
        }
    }

    private fun saveRecord(closeScreenOnSuccess: Boolean) {
        updateState {
            copy(
                isLoading = true,
                error = null,
            )
        }

        viewModelScope.launch {
            val record = uiStateFlow.value.getRecord()
            val flow = if (uiStateFlow.value.isCreateRecordMode) {
                recordUseCase.insert(record)
            } else {
                recordUseCase.update(record)
            }

            flow.catch { exception ->
                updateState {
                    copy(
                        isLoading = false,
                        error = EditRecordUiState.RetryableError(exception.message, Action.OnSaveRecordRetryClick),
                    )
                }
            }.collectLatest {
                updateState {
                    copy(
                        isLoading = false,
                        error = null,
                    )
                }

                if (closeScreenOnSuccess) {
                    sendSideEffect(SideEffect.NavigateBack)
                }
            }
        }
    }
}
