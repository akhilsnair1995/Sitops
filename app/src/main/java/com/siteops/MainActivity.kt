package com.siteops

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.siteops.ui.calculators.CalculatorsScreen
import com.siteops.ui.fieldnotes.FieldNotesViewModel
import com.siteops.ui.fieldnotes.ProjectListScreen
import com.siteops.ui.fieldnotes.ProjectSummaryScreen
import com.siteops.ui.navigation.Screen
import com.siteops.ui.theme.SiteOpsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SiteOpsTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val fieldNotesViewModel: FieldNotesViewModel = viewModel(factory = FieldNotesViewModel.Factory)

    val items = listOf(
        Screen.FieldNotes,
        Screen.Calculators
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.FieldNotes.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.FieldNotes.route) {
                ProjectListScreen(viewModel = fieldNotesViewModel) { project ->
                    navController.navigate(Screen.ProjectDetail.createRoute(project.id))
                }
            }
            
            composable(Screen.Calculators.route) {
                CalculatorsScreen()
            }

            composable(
                route = Screen.ProjectDetail.route,
                arguments = listOf(navArgument("projectId") { type = NavType.LongType })
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable
                val visits by fieldNotesViewModel.getVisits(projectId).collectAsState()
                val project = fieldNotesViewModel.projects.collectAsState().value.find { it.id == projectId }
                
                if (project != null) {
                    ProjectSummaryScreen(project = project, visits = visits)
                }
            }
        }
    }
}
