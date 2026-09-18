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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.core.model.StoryboardShot
import com.example.ui.theme.SaturnTheme
import com.example.ui.viewmodel.SaturnViewModel

@Composable
fun StoryboardScreen(
    viewModel: SaturnViewModel,
    onBack: () -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact

    val shots by viewModel.storyboardShots.collectAsState()
    val currentProject by viewModel.currentProject.collectAsState()
    var showAddShotDialog by remember { mutableStateOf(false) }

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
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = colors.textPrimary)
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text("STORYBOARD STUDIO", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Text(currentProject?.name ?: "Director Breakdown", color = colors.textPrimary, fontSize = if (isCompact) 16.sp else 18.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = { showAddShotDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                    shape = CircleShape,
                    modifier = Modifier.testTag("add_shot_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = if (colors.isDark) colors.background else Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Shot", color = if (colors.isDark) colors.background else Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Summary Bar
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.border, RoundedCornerShape(28.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(if (isCompact) 10.dp else 14.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("TOTAL SHOTS", color = colors.textSecondary, fontSize = 10.sp)
                        Text("${shots.size}", color = colors.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("TOTAL DURATION", color = colors.textSecondary, fontSize = 10.sp)
                        val totalSec = shots.sumOf { it.durationSeconds }
                        Text("${totalSec}s", color = colors.secondaryAccent, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("ASPECT RATIO", color = colors.textSecondary, fontSize = 10.sp)
                        Text("16:9 Cinema", color = colors.primaryAccent, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Shots List
        items(shots) { shot ->
            ShotCard(
                shot = shot,
                colors = colors,
                onGenerate = {
                    viewModel.promptText.value = shot.prompt
                    viewModel.selectedAspectRatio.value = shot.aspectRatio
                    viewModel.selectedDuration.value = shot.durationSeconds
                    viewModel.generate()
                }
            )
        }
    }

    if (showAddShotDialog) {
        AddShotDialog(
            nextShotNum = shots.size + 1,
            colors = colors,
            onConfirm = { scene, shotNum, title, prompt ->
                viewModel.addStoryboardShot(scene, shotNum, title, prompt)
                showAddShotDialog = false
            },
            onDismiss = { showAddShotDialog = false }
        )
    }
}

@Composable
private fun ShotCard(
    shot: StoryboardShot,
    colors: com.example.ui.theme.SaturnColors,
    onGenerate: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, colors.border, RoundedCornerShape(28.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(colors.primaryAccent)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("SCENE ${shot.sceneNumber} • SHOT ${shot.shotNumber}", color = if (colors.isDark) colors.background else Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(shot.title, color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Text("${shot.durationSeconds}s", color = colors.secondaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(shot.prompt, color = colors.textSecondary, fontSize = 13.sp, maxLines = 3)

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (shot.characterName != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(shot.characterName, color = colors.primaryAccent, fontSize = 11.sp)
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Movie, contentDescription = null, tint = colors.textSecondary, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(shot.modelId.take(16), color = colors.textSecondary, fontSize = 10.sp)
                    }
                }

                Button(
                    onClick = onGenerate,
                    colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceCardLight),
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Render Shot", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AddShotDialog(
    nextShotNum: Int,
    colors: com.example.ui.theme.SaturnColors,
    onConfirm: (scene: Int, shotNum: Int, title: String, prompt: String) -> Unit,
    onDismiss: () -> Unit
) {
    var scene by remember { mutableIntStateOf(1) }
    var shotNum by remember { mutableIntStateOf(nextShotNum) }
    var title by remember { mutableStateOf("Hero Action") }
    var prompt by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        containerColor = colors.surfaceCard,
        title = {
            Text("Add Storyboard Shot", color = colors.textPrimary, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Shot Title") },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.primaryAccent,
                        unfocusedBorderColor = colors.border,
                        focusedTextColor = colors.textPrimary,
                        unfocusedTextColor = colors.textPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    label = { Text("Shot Visual Prompt") },
                    placeholder = { Text("Describe subject, action, lighting and camera angle...") },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.primaryAccent,
                        unfocusedBorderColor = colors.border,
                        focusedTextColor = colors.textPrimary,
                        unfocusedTextColor = colors.textPrimary
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (prompt.isNotBlank()) {
                        onConfirm(scene, shotNum, title.trim(), prompt.trim())
                    }
                },
                enabled = prompt.isNotBlank(),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent)
            ) {
                Text("Add to Storyboard", color = if (colors.isDark) colors.background else Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = colors.textSecondary)
            }
        }
    )
}
