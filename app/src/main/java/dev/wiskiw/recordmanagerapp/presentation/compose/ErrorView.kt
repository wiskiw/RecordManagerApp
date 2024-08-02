package dev.wiskiw.recordmanagerapp.presentation.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.wiskiw.recordmanagerapp.R
import dev.wiskiw.recordmanagerapp.presentation.theme.size


@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    text: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.size.four),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = text,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.size.two))
        OutlinedButton(
            onClick = onRetry,
        ) {
            Text(
                modifier = Modifier,
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.compose_error_view_retry_button_label),
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ErrorPreview() {
    ErrorView(
        text = "Here is a preview error message",
        onRetry = {},
    )
}
