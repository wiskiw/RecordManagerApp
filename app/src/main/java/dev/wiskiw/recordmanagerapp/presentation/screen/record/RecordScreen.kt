package dev.wiskiw.recordmanagerapp.presentation.screen.record

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import dev.wiskiw.recordmanagerapp.presentation.theme.RecordManagerTheme

@Composable
fun RecordScreen(
    recordId: String,
    navigateUp: () -> Unit,
) {
    Content(
        recordId = recordId,
    )
}


@Composable
private fun Content(
    modifier: Modifier = Modifier,
    recordId: String,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            modifier = modifier.padding(innerPadding),
            text = "Record $recordId Screen",
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun ContentPreviewLight() {
    RecordManagerTheme(
        darkTheme = false,
    ) {
        Content(
            recordId = "id123"
        )
    }
}
