package com.siteops.ui.fieldnotes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.siteops.data.model.Project
import com.siteops.data.model.SiteVisit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectSummaryScreen(
    project: Project,
    visits: List<SiteVisit>
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Summary: ${project.name}") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Client: ${project.clientName}", style = MaterialTheme.typography.titleMedium)
                Text("Location: ${project.location}", style = MaterialTheme.typography.bodyLarge)
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            items(visits) { visit ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(visit.title, style = MaterialTheme.typography.titleLarge)
                        Text("Date: ${visit.date}", style = MaterialTheme.typography.bodySmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(visit.notes)
                        
                        if (visit.photoUris.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Photos:", style = MaterialTheme.typography.labelLarge)
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                visit.photoUris.take(3).forEach { uri ->
                                    AsyncImage(
                                        model = uri,
                                        contentDescription = null,
                                        modifier = Modifier.size(60.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                                if (visit.photoUris.size > 3) {
                                    Box(modifier = Modifier.size(60.dp), contentAlignment = androidx.compose.ui.Alignment.Center) {
                                        Text("+${visit.photoUris.size - 3}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
