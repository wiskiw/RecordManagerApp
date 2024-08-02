package dev.wiskiw.recordmanagerapp.presentation.screen.recordlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import dev.wiskiw.recordmanagerapp.presentation.theme.RecordManagerTheme
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.ConsumeSideEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecordListScreen(
    viewModel: RecordListViewModel = koinViewModel(),
    navigateToRecord: (String) -> Unit,
) {
    ConsumeSideEffect(
        viewModel = viewModel
    ) { sideEffect: RecordListViewModel.SideEffect ->
        when (sideEffect) {
            is RecordListViewModel.SideEffect.NavigateToRecord -> navigateToRecord(sideEffect.id)
        }
    }

    Content(
        handleAction = viewModel::handleAction,
    )
}


@Composable
private fun Content(
    modifier: Modifier = Modifier,
    handleAction: (RecordListViewModel.Action) -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
        ) {
            Text(
                text = "Record List Screen",
            )
            Button(onClick = { handleAction(RecordListViewModel.Action.OnRecordClick(id = "123")) }) {
                Text(
                    text = "Open Record Details",
                )
            }
        }

    }
}

@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun ContentPreviewLight() {
    RecordManagerTheme(
        darkTheme = false,
    ) {
        Content(
            handleAction = {}
        )
    }
}
