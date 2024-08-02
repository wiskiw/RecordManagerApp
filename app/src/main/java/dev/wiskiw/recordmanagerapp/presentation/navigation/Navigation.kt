package dev.wiskiw.recordmanagerapp.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.wiskiw.recordmanagerapp.presentation.screen.record.RecordScreen
import dev.wiskiw.recordmanagerapp.presentation.screen.recordlist.RecordListScreen

fun NavGraphBuilder.buildMainGraph(navController: NavController) = run {

    composable<AppNavDestination.RecordList> {
        RecordListScreen(
            navigateToRecord = { recordId ->
                val destination = AppNavDestination.Record(id = recordId)
                navController.navigate(destination)
            },
        )
    }

    composable<AppNavDestination.Record> {
        val args = it.toRoute<AppNavDestination.Record>()
        RecordScreen(
            recordId = args.id,
            navigateUp = navController::navigateUp,
        )
    }
}
