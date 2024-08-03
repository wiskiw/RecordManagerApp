package dev.wiskiw.recordmanagerapp.presentation.screen.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.wiskiw.recordmanagerapp.R
import dev.wiskiw.recordmanagerapp.domain.model.Record
import dev.wiskiw.recordmanagerapp.domain.model.RecordType
import dev.wiskiw.recordmanagerapp.domain.model.RecordWithRelations
import dev.wiskiw.recordmanagerapp.presentation.compose.ErrorView
import dev.wiskiw.recordmanagerapp.presentation.compose.ProgressView
import dev.wiskiw.recordmanagerapp.presentation.compose.RecordListView
import dev.wiskiw.recordmanagerapp.presentation.screen.editrecord.EditRecordViewModel
import dev.wiskiw.recordmanagerapp.presentation.theme.RecordManagerTheme
import dev.wiskiw.recordmanagerapp.presentation.theme.size
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.ConsumeSideEffect
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

    ConsumeSideEffect(
        viewModel = viewModel
    ) { sideEffect: RecordViewModel.SideEffect ->
        when (sideEffect) {
            RecordViewModel.SideEffect.NavigateBack -> navigateUp()
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
    state: RecordUiState,
    handleAction: (RecordViewModel.Action) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopBar(handleAction) },
        bottomBar = { BottomBar(handleAction) },
    ) { scaffoldPaddings ->
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    handleAction: (RecordViewModel.Action) -> Unit,
) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        title = {
            Text(stringResource(id = R.string.screen_record_title))
        },
        navigationIcon = {
            IconButton(onClick = { handleAction(RecordViewModel.Action.OnBackClick) }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.screen_record_top_bar_button_back_description),
                )
            }
        },
    )
}

@Composable
private fun BottomBar(
    handleAction: (RecordViewModel.Action) -> Unit,
) {
    BottomAppBar(
        actions = {},
        floatingActionButton = {
            FloatingActionButton(
                onClick = { handleAction(RecordViewModel.Action.OnAddRelationClick) },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, "Localized description")
            }
        }
    )
}

@Composable
private fun RecordWithRelationsContent(
    modifier: Modifier = Modifier,
    recordWithRelations: RecordWithRelations,
) {
    Column(
        modifier = modifier,
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.size.one),
            readOnly = true,
            label = {
                Text(text = stringResource(id = R.string.screen_record_name_title))
            },
            value = recordWithRelations.record.name,
            onValueChange = { },
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.size.one),
            readOnly = true,
            minLines = 4,
            label = {
                Text(text = stringResource(id = R.string.screen_record_description_title))
            },
            value = recordWithRelations.record.description,
            onValueChange = { },
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.size.one),
            readOnly = true,
            label = {
                Text(text = stringResource(id = R.string.screen_record_type_title))
            },
            value = recordWithRelations.record.type.name,
            onValueChange = { },
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = MaterialTheme.size.one,
                    start = MaterialTheme.size.one,
                    end = MaterialTheme.size.one,
                ),
            text = stringResource(id = R.string.screen_record_relations_title),
        )

        RecordListView(
            records = recordWithRelations.relations,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.size.one),
            contentPadding = PaddingValues(
                top = MaterialTheme.size.half,
                bottom = MaterialTheme.size.one,
                start = MaterialTheme.size.one,
                end = MaterialTheme.size.one,
            ),
            onClick = { id ->
                // TODO
            },
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}

@Composable
private fun RelationsPickerDialog(
    handleAction: (RecordViewModel.Action) -> Unit,
    records: List<Record>,
) {
    Dialog(
        onDismissRequest = { handleAction(RecordViewModel.Action.OnDialogDismiss) },
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(MaterialTheme.size.four),
            shape = RoundedCornerShape(MaterialTheme.size.one),
        ) {
            RecordListView(
                contentPadding = PaddingValues(MaterialTheme.size.one),
                records = records,
                onClick = { id -> handleAction(RecordViewModel.Action.OnRelationSelect(id)) }
            )
        }
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
        isRelationsPickerExposed = false
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

@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun RelationsPickerDialogPreview() {
    RelationsPickerDialog(
        handleAction = {},
        records = listOf(
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
}
