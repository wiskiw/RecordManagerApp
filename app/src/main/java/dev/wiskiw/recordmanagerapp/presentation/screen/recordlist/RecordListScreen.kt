package dev.wiskiw.recordmanagerapp.presentation.screen.recordlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.wiskiw.recordmanagerapp.R
import dev.wiskiw.recordmanagerapp.domain.model.Record
import dev.wiskiw.recordmanagerapp.domain.model.RecordType
import dev.wiskiw.recordmanagerapp.presentation.compose.ErrorView
import dev.wiskiw.recordmanagerapp.presentation.compose.ProgressView
import dev.wiskiw.recordmanagerapp.presentation.compose.RecordListView
import dev.wiskiw.recordmanagerapp.presentation.screen.record.RecordViewModel
import dev.wiskiw.recordmanagerapp.presentation.theme.RecordManagerTheme
import dev.wiskiw.recordmanagerapp.presentation.theme.size
import dev.wiskiw.recordmanagerapp.presentation.tool.mvi.ConsumeSideEffect
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecordListScreen(
    viewModel: RecordListViewModel = koinViewModel(),
    navigateToCreateRecord: () -> Unit,
    navigateToEditRecord: (String) -> Unit,
    navigateToRecord: (String) -> Unit,
) {
    ConsumeSideEffect(
        viewModel = viewModel
    ) { sideEffect: RecordListViewModel.SideEffect ->
        when (sideEffect) {
            RecordListViewModel.SideEffect.NavigateToCreateRecordScreen -> navigateToCreateRecord()
            is RecordListViewModel.SideEffect.NavigateToEditRecordScreen -> navigateToEditRecord(sideEffect.id)
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
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopBar() },
        bottomBar = {
            BottomBar(
                handleAction = handleAction,
            )
        }
    ) { scaffoldPaddings ->
        when {
            state.isLoading -> ProgressView(
                modifier = Modifier.padding(scaffoldPaddings),
            )

            state.error != null -> ErrorView(
                modifier = Modifier.padding(scaffoldPaddings),
                text = state.error,
                onRetry = { handleAction(RecordListViewModel.Action.OnRetryClick) },
            )

            else -> RecordListView(
                modifier = Modifier.padding(scaffoldPaddings),
                records = state.records,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.size.one),
                contentPadding = PaddingValues(
                    vertical = MaterialTheme.size.three,
                    horizontal = MaterialTheme.size.one,
                ),
                onClick = { id -> handleAction(RecordListViewModel.Action.OnRecordClick(id)) },
                onEditClick = { id -> handleAction(RecordListViewModel.Action.OnEditClick(id)) },
                onDeleteClick = { id -> handleAction(RecordListViewModel.Action.OnDeleteClick(id)) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        title = {
            Text(stringResource(id = R.string.screen_record_list_title))
        },
    )
}

@Composable
private fun BottomBar(
    handleAction: (RecordListViewModel.Action) -> Unit,
) {
    BottomAppBar(
        actions = {},
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { handleAction(RecordListViewModel.Action.OnAddClick) },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {
                    Icon(Icons.Filled.Add, stringResource(id = R.string.screen_record_list_bottom_bar_button_add_description))
                    Spacer(modifier = Modifier.width(MaterialTheme.size.one))
                    Text(stringResource(id = R.string.screen_record_list_bottom_bar_button_add_title))
                }
            }
        }
    )
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
