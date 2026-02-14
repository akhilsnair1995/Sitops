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
import com.siteops.data.model.SiteVisit

@Composable
fun SiteVisitDetailScreen(
    visit: SiteVisit,
    onAddMarkup: (Offset) -> Unit,
    markups: List<Offset>
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(visit.title, style = MaterialTheme.typography.headlineMedium)
        Text(visit.date.toString(), style = MaterialTheme.typography.bodySmall)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Photo Gallery", style = MaterialTheme.typography.titleMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(visit.photoUris) { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("PDF Markup (Tap to add Red Circle)", style = MaterialTheme.typography.titleMedium)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.LightGray)
        ) {
            // This is where the PDF Renderer would render the page
            // For now, we simulate the PDF page area
            PdfMarkupOverlay(
                markups = markups,
                onTap = onAddMarkup
            )
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
