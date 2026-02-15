package com.siteops.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object FieldNotes : Screen("field_notes", "Field Notes", Icons.Default.List)
    object Calculators : Screen("calculators", "Calculators", Icons.Default.Build)
    
    // Detail screens (not in bottom bar)
    object ProjectDetail : Screen("project_detail/{projectId}", "Project Detail", Icons.Default.List) {
        fun createRoute(projectId: Long) = "project_detail/$projectId"
    }
    object SiteVisitDetail : Screen("site_visit_detail/{visitId}", "Visit Detail", Icons.Default.List) {
        fun createRoute(visitId: Long) = "site_visit_detail/$visitId"
    }
}
