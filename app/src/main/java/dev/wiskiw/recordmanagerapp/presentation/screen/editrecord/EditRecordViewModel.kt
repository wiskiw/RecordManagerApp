package dev.wiskiw.recordmanagerapp.presentation.screen.editrecord

import androidx.lifecycle.SavedStateHandle
import dev.wiskiw.recordmanagerapp.app.logger.AppLogger
import dev.wiskiw.recordmanagerapp.domain.usecase.RecordUseCase
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviAction
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviSideEffect
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.MviViewModel

class EditRecordViewModel(
    savedStateHandle: SavedStateHandle?,
    private val logger: AppLogger,
    private val recordUseCase: RecordUseCase,
) : MviViewModel<EditRecordUiState, EditRecordViewModel.Action, EditRecordViewModel.SideEffect>(savedStateHandle) {

    sealed interface Action : MviAction {
        data object OnBackClick : Action
        data object OnRetryClick : Action
        data object OnSaveClick : Action
        data class OnNameInput(val value: String) : Action
        data class OnDescriptionInput(val value: String) : Action
        data class OnRecordTypeSelected(val uiTypeChip: EditRecordUiState.UiTypeChip) : Action
    }

    sealed interface SideEffect : MviSideEffect {
        data object NavigateBack : SideEffect
    }

    override fun createInitialUiState(): EditRecordUiState = EditRecordUiState(
        recordId = "",
        isLoading = true,
        error = null,
        recordName = "",
        recordDescription = "",
        recordTypeChips = EditRecordUiState.getUiTypeChips(),
    )

    override fun handleAction(action: Action) {
        when (action) {
            Action.OnBackClick -> sendSideEffect(SideEffect.NavigateBack)

            Action.OnRetryClick -> {
                if (uiStateFlow.value.recordId.isNotBlank()) {
//                    fetchRecordWithRelations(uiStateFlow.value.recordId)
                } else {
                    logger.logError("Unexpected state, launchId is empty")
                }
            }

            Action.OnSaveClick -> {
                // todo save changes
                sendSideEffect(SideEffect.NavigateBack)
            }

            is Action.OnNameInput -> {
                updateState {
                    copy(recordName = action.value)
                }
            }

            is Action.OnDescriptionInput -> {
                updateState {
                    copy(recordDescription = action.value)
                }
            }

            is Action.OnRecordTypeSelected -> {
                updateState {
                    val updatedTypeList = recordTypeChips.map {
                        if (it == action.uiTypeChip) {
                            it.copy(selected = !it.selected)
                        } else {
                            it.copy(selected = false)
                        }
                    }
                    copy(recordTypeChips = updatedTypeList)
                }
            }
        }
    }

    fun onArgsReceived(
        recordId: String,
    ) {
        // todo handle recordId
    }
}
