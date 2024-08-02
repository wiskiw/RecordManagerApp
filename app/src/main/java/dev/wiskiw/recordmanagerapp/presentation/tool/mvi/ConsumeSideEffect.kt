package dev.wiskiw.recordmanagerapp.presentation.tool.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow


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
