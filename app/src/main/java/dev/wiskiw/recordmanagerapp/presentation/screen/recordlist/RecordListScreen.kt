package dev.wiskiw.recordmanagerapp.presentation.screen.recordlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.wiskiw.recordmanagerapp.domain.model.Record
import dev.wiskiw.recordmanagerapp.domain.model.RecordType
import dev.wiskiw.recordmanagerapp.presentation.compose.ErrorView
import dev.wiskiw.recordmanagerapp.presentation.compose.ProgressView
import dev.wiskiw.recordmanagerapp.presentation.theme.RecordManagerTheme
import dev.wiskiw.recordmanagerapp.presentation.theme.size
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

    val state by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    Content(
        state = state,
        handleAction = viewModel::handleAction,
    )
}


@Composable
private fun Content(
    modifier: Modifier = Modifier,
    state: RecordListUiState,
    handleAction: (RecordListViewModel.Action) -> Unit,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { scaffoldPaddings ->
        when {
            state.isLoading -> ProgressView(
                modifier = Modifier.padding(scaffoldPaddings),
            )

            state.error != null -> ErrorView(
                modifier = Modifier.padding(scaffoldPaddings),
                text = state.error,
                onRetry = { handleAction(RecordListViewModel.Action.OnRetryClick) },
            )

            else -> RecordList(
                modifier = Modifier.padding(scaffoldPaddings),
                records = state.records
            ) { id ->
                val action = RecordListViewModel.Action.OnRecordClick(id)
                handleAction(action)
            }
        }
    }
}

@Composable
private fun RecordList(
    modifier: Modifier = Modifier,
    records: List<Record>,
    onClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.size.one)
    ) {
        item {
            Spacer(modifier = Modifier.height(MaterialTheme.size.three))
        }
        items(records.size) {
            val record = records[it]
            RecordItem(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.size.one)
                    .fillMaxWidth(),
                record = record,
                onClick = {
                    onClick(record.id)
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(MaterialTheme.size.three))
        }
    }
}

@Composable
private fun RecordItem(
    modifier: Modifier = Modifier,
    record: Record,
    onClick: () -> Unit = {},
) {
    val shape = RoundedCornerShape(MaterialTheme.size.one)
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = shape,
            )
            .clip(shape)
            .clickable { onClick.invoke() }
            .padding(MaterialTheme.size.one),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.size.one),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.size.half),
        ) {
            Text(
                text = record.id,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = record.name,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun ContentPreviewLight() {
    val records = listOf(
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
    )

    val uiState = RecordListUiState(
        isLoading = false,
        error = null,
        records = records
    )

    RecordManagerTheme(
        darkTheme = false,
    ) {
        Content(
            state = uiState,
            handleAction = {}
        )
    }
}
