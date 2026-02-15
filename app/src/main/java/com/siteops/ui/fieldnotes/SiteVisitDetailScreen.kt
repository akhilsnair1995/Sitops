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
import android.net.Uri
import android.content.Intent
import androidx.compose.ui.viewinterop.AndroidView
import com.github.barteksc.pdfviewer.PDFView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Undo
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
    val context = LocalContext.current
    var photoUris by remember { mutableStateOf(visit.photoUris) }
    var pdfUriString by remember { mutableStateOf(visit.pdfUri) }
    var markups by remember { mutableStateOf(viewModel.deserializeMarkups(visit.markupsJson)) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            uris.forEach { uri ->
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: Exception) {
                    // Log or handle error if needed
                }
            }
            photoUris = photoUris + uris.map { it.toString() }
        }
    )

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                try {
                    context.contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: Exception) {
                    // Log or handle error if needed
                }
                pdfUriString = it.toString()
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(visit.title) },
                actions = {
                    IconButton(onClick = { markups = emptyList() }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear All")
                    }
                    IconButton(onClick = { if (markups.isNotEmpty()) markups = markups.dropLast(1) }) {
                        Icon(Icons.Default.Undo, contentDescription = "Undo")
                    }
                    IconButton(onClick = {
                        val updatedVisit = visit.copy(
                            photoUris = photoUris,
                            pdfUri = pdfUriString,
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

            LazyRow(
                modifier = Modifier.fillMaxWidth().height(120.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(photoUris) { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier.size(120.dp).background(Color.DarkGray),
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
                Text("PDF Markup (Tap to Circle)", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = { pdfPickerLauncher.launch(arrayOf("application/pdf")) }) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = "Select PDF")
                }
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White)
            ) {
                if (pdfUriString != null) {
                    val uri = Uri.parse(pdfUriString)
                    AndroidView(
                        factory = { ctx ->
                            PDFView(ctx, null).apply {
                                fromUri(uri)
                                    .enableAnnotationRendering(true)
                                    .scrollHandle(null)
                                    .load()
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text("No PDF Selected", color = Color.Gray)
                    }
                }

                // Overlay remains on top of the PDFView
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
