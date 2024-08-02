package dev.wiskiw.recordmanagerapp.presentation.screen.editrecord

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.wiskiw.recordmanagerapp.R
import dev.wiskiw.recordmanagerapp.presentation.compose.ErrorView
import dev.wiskiw.recordmanagerapp.presentation.compose.ProgressView
import dev.wiskiw.recordmanagerapp.presentation.theme.RecordManagerTheme
import dev.wiskiw.recordmanagerapp.presentation.theme.size
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditRecordScreen(
    viewModel: EditRecordViewModel = koinViewModel(),
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
    state: EditRecordUiState,
    handleAction: (EditRecordViewModel.Action) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopBar(handleAction) },
    ) { scaffoldPaddings ->
        when {
            state.isLoading -> ProgressView(
                modifier = Modifier.padding(scaffoldPaddings),
            )

            state.error != null -> ErrorView(
                modifier = Modifier.padding(scaffoldPaddings),
                text = state.error,
                onRetry = { handleAction(EditRecordViewModel.Action.OnRetryClick) },
            )

            else -> EditRecordContent(
                modifier = Modifier.padding(scaffoldPaddings),
                name = state.recordName,
                description = state.recordDescription,
                typeChips = state.recordTypeChips,
                handleAction = handleAction,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    handleAction: (EditRecordViewModel.Action) -> Unit,

    ) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        title = {
            Text(stringResource(id = R.string.screen_edit_title))
        },
        navigationIcon = {
            IconButton(onClick = {
                handleAction(EditRecordViewModel.Action.OnBackClick)
            }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Navigate back")
            }
        },
        actions = {

            IconButton(onClick = { handleAction(EditRecordViewModel.Action.OnSaveClick) }) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.screen_edit_tob_but_button_save_description),
                )
            }
        },
    )
}

@Composable
private fun EditRecordContent(
    modifier: Modifier = Modifier,
    name: String,
    description: String,
    typeChips: List<EditRecordUiState.UiTypeChip>,
    handleAction: (EditRecordViewModel.Action) -> Unit,
) {
    Column(modifier = modifier) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(MaterialTheme.size.one),
            label = {
                Text(text = stringResource(id = R.string.screen_edit_record_name_title))
            },
            value = name,
            onValueChange = { value -> handleAction(EditRecordViewModel.Action.OnNameInput(value)) },
        )

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(MaterialTheme.size.one),
            minLines = 4,
            label = {
                Text(text = stringResource(id = R.string.screen_edit_record_description_title))
            },
            value = description,
            onValueChange = { value -> handleAction(EditRecordViewModel.Action.OnDescriptionInput(value)) },
        )

        LazyVerticalStaggeredGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.size.one),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            columns = StaggeredGridCells.Fixed(3),
            content = {
                items(typeChips) { type ->
                    FilterChip(
                        selected = type.selected,
                        label = {
                            Text(
                                modifier = Modifier.fillMaxSize(),
                                text = type.label,
                                textAlign = TextAlign.Center,
                            )
                        },
                        onClick = { handleAction(EditRecordViewModel.Action.OnRecordTypeSelected(type)) },
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
private fun ContentPreviewLight() {
    val state = EditRecordUiState(
        recordId = "recordId",
        isLoading = false,
        error = null,

        recordDescription = "made of wood with metal frame",
        recordName = "TAble 1f",
        recordTypeChips = EditRecordUiState.getUiTypeChips(),
    )

    RecordManagerTheme(
        darkTheme = false,
    ) {
        Content(state = state, handleAction = {})
    }
}
