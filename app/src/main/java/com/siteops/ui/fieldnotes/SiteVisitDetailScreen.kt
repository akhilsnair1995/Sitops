package com.siteops.ui.fieldnotes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Save
import com.siteops.data.model.SiteVisit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteVisitDetailScreen(
    visit: SiteVisit,
    viewModel: FieldNotesViewModel,
    onBack: () -> Unit
) {
    var photoUris by remember { mutableStateOf(visit.photoUris) }
    var pdfUri by remember { mutableStateOf(visit.pdfUri) }
    var markups by remember { mutableStateOf(viewModel.deserializeMarkups(visit.markupsJson)) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> photoUris = photoUris + uris.map { it.toString() } }
    )

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> pdfUri = uri?.toString() }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(visit.title) },
                actions = {
                    IconButton(onClick = {
                        val updatedVisit = visit.copy(
                            photoUris = photoUris,
                            pdfUri = pdfUri,
                            markupsJson = viewModel.serializeMarkups(markups)
                        )
                        viewModel.updateSiteVisit(updatedVisit)
                        onBack()
                    }) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text("Photo Gallery", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Add Photos")
                }
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(photoUris) { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Text("PDF Markup", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = { pdfPickerLauncher.launch("application/pdf") }) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = "Select PDF")
                }
            }
            
            if (pdfUri != null) {
                Text("Selected: ${pdfUri?.split("/")?.last()}", style = MaterialTheme.typography.bodySmall)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.LightGray)
            ) {
                // Future: Use AndroidView to host PDFView
                PdfMarkupOverlay(
                    markups = markups,
                    onTap = { markups = markups + it }
                )
            }
        }
    }
}

@Composable
fun PdfMarkupOverlay(
    markups: List<Offset>,
    onTap: (Offset) -> Unit
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    onTap(offset)
                }
            }
    ) {
        markups.forEach { center ->
            drawCircle(
                color = Color.Red,
                center = center,
                radius = 40f,
                style = Stroke(width = 8f)
            )
        }
    }
}
