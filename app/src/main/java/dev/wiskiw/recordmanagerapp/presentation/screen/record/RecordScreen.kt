package dev.wiskiw.recordmanagerapp.presentation.screen.record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.wiskiw.recordmanagerapp.domain.model.Record
import dev.wiskiw.recordmanagerapp.domain.model.RecordType
import dev.wiskiw.recordmanagerapp.domain.model.RecordWithRelations
import dev.wiskiw.recordmanagerapp.presentation.compose.ErrorView
import dev.wiskiw.recordmanagerapp.presentation.compose.ProgressView
import dev.wiskiw.recordmanagerapp.presentation.compose.RecordListView
import dev.wiskiw.recordmanagerapp.presentation.theme.RecordManagerTheme
import dev.wiskiw.recordmanagerapp.presentation.theme.size
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
        state = state,
        handleAction = viewModel::handleAction,
    )
}


@Composable
private fun Content(
    modifier: Modifier = Modifier,
    state: RecordUiState,
    handleAction: (RecordViewModel.Action) -> Unit,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { scaffoldPaddings ->
        when {
            state.isLoading -> ProgressView(
                modifier = Modifier.padding(scaffoldPaddings),
            )

            state.error != null -> ErrorView(
                modifier = Modifier.padding(scaffoldPaddings),
                text = state.error,
                onRetry = { handleAction(RecordViewModel.Action.OnRetryClick) },
            )

            state.recordWithRelations != null -> RecordWithRelationsContent(
                modifier = Modifier.padding(scaffoldPaddings),
                recordWithRelations = state.recordWithRelations,
            )
        }
    }
}

@Composable
private fun RecordWithRelationsContent(
    modifier: Modifier = Modifier,
    recordWithRelations: RecordWithRelations,
) {
    Column(modifier = modifier) {
        val shape = RoundedCornerShape(MaterialTheme.size.one)
        Row(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.size.one, vertical = MaterialTheme.size.three)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = shape,
                )
                .fillMaxWidth()
                .heightIn(min = MaterialTheme.size.twentyOne)
                .clip(shape)
                .padding(MaterialTheme.size.one),

            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.size.one),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.size.half),
            ) {
                Text(
                    text = recordWithRelations.record.id,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = recordWithRelations.record.name,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        RecordListView(
            records = recordWithRelations.relations,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.size.one),
            contentPadding = PaddingValues(
                vertical = MaterialTheme.size.three,
                horizontal = MaterialTheme.size.one,
            ),
            onClick = { id ->
                // TODO
            },
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun ContentPreviewLight() {
    val recordWithRelations = RecordWithRelations(
        record = Record(
            id = "main123",
            type = RecordType.Desk,
            name = "Main Record",
            description = "description here",
        ),
        relations = listOf(
            Record(
                id = "r-1",
                type = RecordType.Desk,
                name = "name-1",
                description = "description-1",
            ),
            Record(
                id = "r-2",
                type = RecordType.Server,
                name = "name-2",
                description = "description-2",
            ),
            Record(
                id = "r-3",
                type = RecordType.Employee,
                name = "name-3",
                description = "description-3",
            ),
        ),
    )

    val state = RecordUiState(
        recordId = recordWithRelations.record.id,
        recordWithRelations = recordWithRelations,
        isLoading = false,
        error = null,
    )

    RecordManagerTheme(
        darkTheme = false,
    ) {
        Content(
            state = state,
            handleAction = {}
        )
    }
}
