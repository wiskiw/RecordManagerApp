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

@Composable
fun RecordListScreen(
    navigateToRecord: (String) -> Unit,
) {
    Content(
        navigateToRecord = navigateToRecord,
    )
}


@Composable
private fun Content(
    modifier: Modifier = Modifier,
    navigateToRecord: (String) -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding),
        ) {
            Text(
                text = "Record List Screen",
            )
            Button(onClick = { navigateToRecord("123") }) {
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
            navigateToRecord = {}
        )
    }
}
