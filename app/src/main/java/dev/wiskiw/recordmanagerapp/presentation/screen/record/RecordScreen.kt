package dev.wiskiw.recordmanagerapp.presentation.screen.record

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.wiskiw.recordmanagerapp.presentation.theme.RecordManagerTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecordScreen(
    viewModel: RecordViewModel = koinViewModel(),
    recordId: String,
    navigateUp: () -> Unit,
) {
    LaunchedEffect(recordId) {
        viewModel.onArgsReceived(
            recordId = recordId,
        )
    }

    val state by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    Content(
        state = state
    )
}


@Composable
private fun Content(
    modifier: Modifier = Modifier,
    state: RecordUiState,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            modifier = modifier.padding(innerPadding),
            text = "Record ${state.recordId} Screen",
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun ContentPreviewLight() {
    val state = RecordUiState(
        recordId = "preview123",
    )

    RecordManagerTheme(
        darkTheme = false,
    ) {
        Content(
            state = state
        )
    }
}
