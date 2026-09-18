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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.SaturnProject
import com.example.ui.theme.SaturnTheme
import com.example.ui.viewmodel.SaturnViewModel

@Composable
fun ProjectsScreen(
    viewModel: SaturnViewModel,
    onNavigate: (String) -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact

    val projects by viewModel.allProjects.collectAsState()
    val currentProject by viewModel.currentProject.collectAsState()
    var projectToExport by remember { mutableStateOf<SaturnProject?>(null) }
    var exportSuccessMessage by remember { mutableStateOf<String?>(null) }

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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("PROJECTS VAULT", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text("Creative Projects", color = colors.textPrimary, fontSize = if (isCompact) 18.sp else 20.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { viewModel.showNewProjectDialog.value = true },
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                    shape = CircleShape,
                    modifier = Modifier.testTag("create_new_project_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = if (colors.isDark) colors.background else Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Project", color = if (colors.isDark) colors.background else Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        items(projects) { project ->
            val isCurrent = project.id == currentProject?.id
            ProjectCard(
                project = project,
                isCurrent = isCurrent,
                colors = colors,
                isCompact = isCompact,
                onSelect = {
                    viewModel.selectProject(project)
                },
                onExport = {
                    projectToExport = project
                },
                onOpenStoryboard = {
                    viewModel.selectProject(project)
                    onNavigate("storyboard")
                }
            )
        }
    }

    // Export Confirmation Dialog
    projectToExport?.let { proj ->
        AlertDialog(
            onDismissRequest = { projectToExport = null },
            shape = RoundedCornerShape(28.dp),
            containerColor = colors.surfaceCard,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FileDownload, contentDescription = null, tint = colors.primaryAccent)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Export Project: ${proj.name}", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Choose export packaging option for this project:", color = colors.textSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    listOf(
                        "Master 4K Video Sequence (ProRes / H.265)",
                        "Complete Storyboard & Shot Prompts (PDF / JSON)",
                        "Raw Project Archive with Audio & Stems (ZIP)"
                    ).forEach { format ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(CircleShape)
                                .background(colors.surfaceCardLight)
                                .border(1.dp, colors.border, CircleShape)
                                .clickable {
                                    projectToExport = null
                                    exportSuccessMessage = "Successfully exported ${proj.name} [$format] to device storage."
                                }
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text(format, color = colors.secondaryAccent, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { projectToExport = null }) {
                    Text("Cancel", color = colors.textSecondary)
                }
            }
        )
    }

    // Export Success Dialog
    exportSuccessMessage?.let { msg ->
        AlertDialog(
            onDismissRequest = { exportSuccessMessage = null },
            shape = RoundedCornerShape(28.dp),
            containerColor = colors.surfaceCard,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF34D399))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Export Completed", color = colors.textPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(msg, color = colors.textSecondary, fontSize = 13.sp)
            },
            confirmButton = {
                Button(
                    onClick = { exportSuccessMessage = null },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent)
                ) {
                    Text("OK", color = if (colors.isDark) colors.background else Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun ProjectCard(
    project: SaturnProject,
    isCurrent: Boolean,
    colors: com.example.ui.theme.SaturnColors,
    isCompact: Boolean,
    onSelect: () -> Unit,
    onExport: () -> Unit,
    onOpenStoryboard: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isCurrent) colors.primaryAccent else colors.border,
                RoundedCornerShape(28.dp)
            )
    ) {
        Column(modifier = Modifier.padding(if (isCompact) 12.dp else 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(colors.surfaceCardLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Folder, contentDescription = null, tint = if (isCurrent) colors.primaryAccent else colors.secondaryAccent, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(project.name, color = colors.textPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text(project.version, color = colors.secondaryAccent, fontSize = 11.sp)
                    }
                }

                if (isCurrent) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(colors.primaryAccent)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("ACTIVE", color = if (colors.isDark) colors.background else Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(project.description, color = colors.textSecondary, fontSize = 12.sp, maxLines = 2)

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${project.sceneCount} Scenes • ${project.shotCount} Director Shots",
                    color = colors.textSecondary,
                    fontSize = 11.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = onExport, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.FileDownload, contentDescription = "Export", tint = colors.secondaryAccent, modifier = Modifier.size(18.dp))
                    }

                    Button(
                        onClick = onOpenStoryboard,
                        colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceCardLight),
                        shape = CircleShape
                    ) {
                        Text("Open Storyboard", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
