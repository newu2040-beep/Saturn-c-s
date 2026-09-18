package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.GenerationJob
import com.example.core.model.JobStatus
import com.example.ui.theme.SaturnTheme
import com.example.ui.viewmodel.SaturnViewModel

@Composable
fun GenerationQueueScreen(
    viewModel: SaturnViewModel,
    onBack: () -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact

    val activeQueue by viewModel.activeQueue.collectAsState()
    val allGenerations by viewModel.allGenerations.collectAsState()
    var inspectedJob by remember { mutableStateOf<GenerationJob?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = if (isCompact) 8.dp else 16.dp),
        verticalArrangement = Arrangement.spacedBy(if (isCompact) 10.dp else 14.dp),
        contentPadding = PaddingValues(top = if (isCompact) 4.dp else 8.dp, bottom = 100.dp)
    ) {
        // Header
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = colors.textPrimary)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text("GENERATION QUEUE & LOGS", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text("Real-time AI Processing Pipeline", color = colors.textPrimary, fontSize = if (isCompact) 15.sp else 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Active Tasks Section
        if (activeQueue.isNotEmpty()) {
            item {
                Text(
                    text = "ACTIVE JOBS IN PROGRESS (${activeQueue.size})",
                    color = colors.secondaryAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(activeQueue) { job ->
                ActiveJobCard(
                    job = job,
                    onCancel = { viewModel.cancelJob(job.id) },
                    onInspect = { inspectedJob = job }
                )
            }
        }

        // Completed / Historic Generations Section
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "GENERATION HISTORY & LOGS",
                color = colors.textSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (allGenerations.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No generation jobs yet", color = colors.textSecondary, fontSize = 14.sp)
                        Text("Start creating in the Studio to view live pipeline logs here.", color = colors.textSecondary, fontSize = 12.sp)
                    }
                }
            }
        } else {
            items(allGenerations) { job ->
                HistoricJobCard(
                    job = job,
                    onDelete = { viewModel.deleteJob(job.id) },
                    onInspect = { inspectedJob = job },
                    onPreview = { viewModel.openMediaPreview(job) },
                    onRetry = {
                        viewModel.promptText.value = job.prompt
                        viewModel.generate()
                    }
                )
            }
        }
    }

    // Raw Payload Inspector Dialog
    inspectedJob?.let { job ->
        AlertDialog(
            onDismissRequest = { inspectedJob = null },
            shape = RoundedCornerShape(28.dp),
            containerColor = colors.surfaceCard,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Code, contentDescription = null, tint = colors.secondaryAccent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("API Payload Inspector", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("REQUEST PAYLOAD", color = colors.primaryAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = job.rawRequest ?: "{\n  \"model\": \"${job.modelId}\",\n  \"prompt\": \"${job.prompt}\"\n}",
                        color = colors.textPrimary,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 12.dp)
                            .background(colors.surfaceCardLight, RoundedCornerShape(20.dp))
                            .padding(12.dp)
                    )

                    Text("RESPONSE METADATA", color = colors.secondaryAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = job.rawResponse ?: "{\n  \"status\": \"${job.status.name}\",\n  \"output\": \"${job.outputUrls.firstOrNull() ?: "generating"}\"\n}",
                        color = colors.textPrimary,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .background(colors.surfaceCardLight, RoundedCornerShape(20.dp))
                            .padding(12.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { inspectedJob = null }) {
                    Text("Close", color = colors.secondaryAccent)
                }
            }
        )
    }
}

@Composable
private fun ActiveJobCard(
    job: GenerationJob,
    onCancel: () -> Unit,
    onInspect: () -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.secondaryAccent, RoundedCornerShape(28.dp))
    ) {
        Column(modifier = Modifier.padding(if (isCompact) 10.dp else 14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(colors.secondaryAccent.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(job.status.label, color = colors.secondaryAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(job.modelId.take(18), color = colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                IconButton(onClick = onCancel, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Cancel, contentDescription = "Cancel", tint = Color(0xFFFB7185), modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(job.prompt, color = colors.textSecondary, fontSize = 12.sp, maxLines = 2)

            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { job.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = colors.primaryAccent,
                trackColor = colors.surfaceCardLight
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${(job.progress * 100).toInt()}% Rendered", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text("Inspect JSON", color = colors.secondaryAccent, fontSize = 11.sp, modifier = Modifier.clickable(onClick = onInspect))
            }
        }
    }
}

@Composable
private fun HistoricJobCard(
    job: GenerationJob,
    onDelete: () -> Unit,
    onInspect: () -> Unit,
    onPreview: () -> Unit,
    onRetry: () -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.border, RoundedCornerShape(28.dp))
    ) {
        Column(modifier = Modifier.padding(if (isCompact) 10.dp else 14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val statusColor = when (job.status) {
                        JobStatus.COMPLETED -> Color(0xFF34D399)
                        JobStatus.FAILED -> Color(0xFFFB7185)
                        else -> colors.textSecondary
                    }
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(job.status.label, color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(job.modelId.take(16), color = colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Row {
                    IconButton(onClick = onInspect, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Code, contentDescription = "Inspect", tint = colors.secondaryAccent, modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = colors.textSecondary, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(job.prompt, color = colors.textSecondary, fontSize = 12.sp, maxLines = 2)

            if (job.errorMessage != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(job.errorMessage, color = Color(0xFFFB7185), fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (job.status == JobStatus.COMPLETED) {
                    Button(
                        onClick = onPreview,
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent.copy(alpha = 0.2f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.primaryAccent),
                        shape = CircleShape,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Preview & Master", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text(job.estimatedCost, color = colors.textSecondary, fontSize = 11.sp)
                }

                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceCardLight),
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Re-run Prompt", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
