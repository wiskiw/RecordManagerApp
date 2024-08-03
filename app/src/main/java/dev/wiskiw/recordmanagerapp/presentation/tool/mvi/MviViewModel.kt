package dev.wiskiw.recordmanagerapp.presentation.tool.mvi

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MviViewModel<State : Any, Action : MviAction, SideEffect : MviSideEffect>(
    private val savedStateHandle: SavedStateHandle?,
) : ViewModel() {

    /**
     * A channel that emits side effects to be consumed by the view.
     * Use [sideEffect] to access side effects from a View.
     */
    private val sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffect: Flow<SideEffect>
        get() = sideEffectChannel.receiveAsFlow()

    /**
     * The key used to save and restore the UI state in [SavedStateHandle].
     * Default is the class name.
     */
    protected open val uiStateKey: String = this.javaClass.name

    /**
    * The initial UI state, either restored from [SavedStateHandle] or created by [createInitialUiState].
    */
    private val initialUiState: State by lazy { savedStateHandle?.get<State>(uiStateKey) ?: createInitialUiState() }


    /**
     * A mutable state flow that holds the current UI state.
     * Use [uiStateFlow] to access state from a View.
     */
    private val internalUiStateFlow = MutableStateFlow(initialUiState)
    val uiStateFlow: StateFlow<State> = internalUiStateFlow.asStateFlow()

    /**
     * Creates the initial UI state when no saved state is available.
     */
    abstract fun createInitialUiState(): State

    /**
     * Updates the current UI state using [updater] function.
     *
     * @param updater A lambda function that takes the current state and returns the updated state.
     */
    protected fun updateState(updater: State.() -> State) {
        internalUiStateFlow.update {
            val newState = updater.invoke(it)

            // Save updated state to savedStateHandle if possible
            if (savedStateHandle != null && newState is Parcelable) {
                savedStateHandle[uiStateKey] = newState
            }

            newState
        }
    }

    /**
     * Handles an action dispatched from the view.
     */
    abstract fun handleAction(action: Action)

    /**
     * Sends a side effect to the view. Side effects are intended to be consumed only once.
     */
    protected fun sendSideEffect(sideEffect: SideEffect) {
        viewModelScope.launch {
            sideEffectChannel.send(sideEffect)
        }
    }
}
