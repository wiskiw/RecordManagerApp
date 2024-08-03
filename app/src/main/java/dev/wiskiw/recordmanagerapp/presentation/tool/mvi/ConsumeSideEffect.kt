package dev.wiskiw.recordmanagerapp.presentation.tool.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

/**
 * Collects side effects from a [Flow] and invokes the [sideEffectHandler] when a new side effect is emitted.
 *
 * This function uses the [Lifecycle] of the current [LocalLifecycleOwner] to manage the collection of side effects.
 * The collection starts when the lifecycle reaches the specified [lifeCycleState]
 * and is automatically stopped when the lifecycle goes below that state.
 */
@Composable
fun <SideEffect : MviSideEffect> ConsumeSideEffect(
    sideEffectFlow: Flow<SideEffect>,
    lifeCycleState: Lifecycle.State = Lifecycle.State.STARTED,
    sideEffectHandler: (SideEffect) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(sideEffectFlow) {
        lifecycleOwner.repeatOnLifecycle(lifeCycleState) {
            sideEffectFlow.collect(sideEffectHandler)
        }
    }
}

/**
 * Collects side effects from an [MviViewModel] and invokes the [sideEffectHandler] when a new side effect is emitted.
 *
 * @see ConsumeSideEffect
 */
@Composable
fun <State : Any, Action : MviAction, SideEffect : MviSideEffect> ConsumeSideEffect(
    viewModel: MviViewModel<State, Action, SideEffect>,
    lifeCycleState: Lifecycle.State = Lifecycle.State.STARTED,
    sideEffectHandler: (SideEffect) -> Unit,
) {
    ConsumeSideEffect(
        sideEffectFlow = viewModel.sideEffect,
        lifeCycleState = lifeCycleState,
        sideEffectHandler = sideEffectHandler,
    )
}
