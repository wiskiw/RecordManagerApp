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

    private val sideEffectChannel = Channel<SideEffect>(capacity = Channel.BUFFERED)
    val sideEffect: Flow<SideEffect>
        get() = sideEffectChannel.receiveAsFlow()


    protected open val uiStateKey: String = this.javaClass.name
    private val initialUiState: State by lazy { savedStateHandle?.get<State>(uiStateKey) ?: createInitialUiState() }

    private val internalUiStateFlow = MutableStateFlow(initialUiState)
    val uiStateFlow: StateFlow<State> = internalUiStateFlow.asStateFlow()

    abstract fun createInitialUiState(): State

    protected fun updateState(updater: State.() -> State) {
        internalUiStateFlow.update {
            val newState = updater.invoke(it)

            // Save updated state to savedStateHandle id possible
            if (savedStateHandle != null && newState is Parcelable) {
                savedStateHandle[uiStateKey] = newState
            }

            newState
        }
    }

    abstract fun handleAction(action: Action)

    /**
     *  Send single-time-side-effect to the View
     */
    protected fun sendSideEffect(sideEffect: SideEffect) {
        viewModelScope.launch {
            sideEffectChannel.send(sideEffect)
        }
    }
}
