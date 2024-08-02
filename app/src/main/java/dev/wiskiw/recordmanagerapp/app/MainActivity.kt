package dev.wiskiw.recordmanagerapp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.wiskiw.recordmanagerapp.presentation.navigation.AppNavDestination
import dev.wiskiw.recordmanagerapp.presentation.navigation.buildMainGraph
import dev.wiskiw.recordmanagerapp.presentation.theme.RecordManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecordManagerTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = AppNavDestination.RecordList
                ) {
                    with(this) { buildMainGraph(navController) }
                }
            }
        }
    }
}
