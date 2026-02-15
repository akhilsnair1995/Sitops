package com.siteops.ui.fieldnotes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Summarize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.siteops.data.model.Project
import com.siteops.data.model.SiteVisit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    project: Project,
    viewModel: FieldNotesViewModel,
    onVisitClick: (SiteVisit) -> Unit,
    onSummaryClick: () -> Unit
) {
    val visits by viewModel.getVisits(project.id).collectAsState()
    var showAddVisitDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(project.name) },
                actions = {
                    IconButton(onClick = onSummaryClick) {
                        Icon(Icons.Default.Summarize, contentDescription = "View Summary")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddVisitDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Visit")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (visits.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("No visits recorded yet.", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(visits) { visit ->
                        ElevatedCard(
                            modifier = Modifier.fillMaxWidth().clickable { onVisitClick(visit) }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(visit.title, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    text = java.text.DateFormat.getDateInstance().format(java.util.Date(visit.date)),
                                    style = MaterialTheme.typography.bodySmall
                                )
                                if (visit.notes.isNotEmpty()) {
                                    Text(
                                        text = visit.notes,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 2,
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showAddVisitDialog) {
            AddVisitDialog(
                onDismiss = { showAddVisitDialog = false },
                onConfirm = { title, notes ->
                    viewModel.addSiteVisit(project.id, title, notes, emptyList(), null)
                    showAddVisitDialog = false
                }
            )
        }
    }
}

@Composable
fun AddVisitDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Site Visit") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Visit Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(onClick = { if (title.isNotBlank()) onConfirm(title, notes) }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
