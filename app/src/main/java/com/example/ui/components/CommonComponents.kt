package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.AiModel
import com.example.core.model.AiProvider
import com.example.core.model.SaturnProject
import com.example.core.prompt.PromptEnhancer
import com.example.ui.theme.GlassBackground
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.SaturnBorder
import com.example.ui.theme.SaturnBorderHighlight
import com.example.ui.theme.SaturnCyan
import com.example.ui.theme.SaturnGold
import com.example.ui.theme.SaturnObsidian
import com.example.ui.theme.SaturnSurfaceCard
import com.example.ui.theme.SaturnTextPrimary
import com.example.ui.theme.SaturnTextSecondary
import com.example.ui.theme.SaturnViolet

import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FitScreen
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import com.example.ui.theme.SaturnTheme
import com.example.ui.theme.SaturnThemePalette

@Composable
fun SaturnTopBar(
    currentProject: SaturnProject?,
    allProjects: List<SaturnProject>,
    selectedProvider: AiProvider,
    selectedModel: AiModel,
    activeQueueCount: Int,
    isDarkMode: Boolean = true,
    onToggleDarkMode: () -> Unit = {},
    currentPalette: SaturnThemePalette = SaturnThemePalette.OBSIDIAN_GOLD,
    onSelectPalette: (SaturnThemePalette) -> Unit = {},
    isCompactMode: Boolean = false,
    onToggleCompactMode: () -> Unit = {},
    onSelectProject: (SaturnProject) -> Unit,
    onNewProjectClick: () -> Unit,
    onQueueClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var showProjectDropdown by remember { mutableStateOf(false) }
    var showPaletteDropdown by remember { mutableStateOf(false) }
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact || isCompactMode

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(horizontal = if (isCompact) 8.dp else 16.dp, vertical = if (isCompact) 4.dp else 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Project Selector Pill
        Box {
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colors.surfaceCard)
                    .border(1.dp, colors.border, CircleShape)
                    .clickable { showProjectDropdown = true }
                    .padding(horizontal = if (isCompact) 8.dp else 12.dp, vertical = if (isCompact) 4.dp else 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(if (isCompact) 18.dp else 22.dp)
                        .clip(CircleShape)
                        .border(1.dp, colors.primaryAccent, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.saturn_circle_icon_1789712389721),
                        contentDescription = "Saturn Logo",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currentProject?.name ?: "SATURN C",
                    color = colors.textPrimary,
                    fontSize = if (isCompact) 11.sp else 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = if (isCompact) 90.dp else 130.dp)
                )
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Select Project",
                    tint = colors.textSecondary,
                    modifier = Modifier.size(14.dp)
                )
            }

            DropdownMenu(
                expanded = showProjectDropdown,
                onDismissRequest = { showProjectDropdown = false },
                modifier = Modifier.background(colors.surfaceCard)
            ) {
                allProjects.forEach { project ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = project.name,
                                color = if (project.id == currentProject?.id) colors.primaryAccent else colors.textPrimary
                            )
                        },
                        onClick = {
                            onSelectProject(project)
                            showProjectDropdown = false
                        }
                    )
                }
                DropdownMenuItem(
                    leadingIcon = { Icon(Icons.Default.Add, contentDescription = null, tint = colors.secondaryAccent) },
                    text = { Text("New Project...", color = colors.secondaryAccent) },
                    onClick = {
                        showProjectDropdown = false
                        onNewProjectClick()
                    }
                )
            }
        }

        // Right side controls: Compact Mode, Palette Picker, Dark/Light Mode, Queue, Settings
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Compact Mode Toggle Pill / Icon
            IconButton(
                onClick = onToggleCompactMode,
                modifier = Modifier
                    .size(if (isCompact) 32.dp else 36.dp)
                    .testTag("compact_mode_toggle")
            ) {
                Icon(
                    imageVector = if (isCompact) Icons.Default.FitScreen else Icons.Default.AspectRatio,
                    contentDescription = "Toggle Compact Mode",
                    tint = if (isCompact) colors.primaryAccent else colors.textSecondary,
                    modifier = Modifier.size(if (isCompact) 18.dp else 20.dp)
                )
            }

            // Pastel Theme Palette Picker Button
            Box {
                IconButton(
                    onClick = { showPaletteDropdown = true },
                    modifier = Modifier
                        .size(if (isCompact) 32.dp else 36.dp)
                        .testTag("theme_palette_picker")
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Theme Palette",
                        tint = colors.primaryAccent,
                        modifier = Modifier.size(if (isCompact) 18.dp else 20.dp)
                    )
                }

                DropdownMenu(
                    expanded = showPaletteDropdown,
                    onDismissRequest = { showPaletteDropdown = false },
                    modifier = Modifier.background(colors.surfaceCard)
                ) {
                    SaturnThemePalette.values().forEach { palette ->
                        DropdownMenuItem(
                            leadingIcon = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(palette.previewColor)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(palette.secondaryPreview)
                                    )
                                }
                            },
                            text = {
                                Text(
                                    text = palette.label,
                                    color = if (palette == currentPalette) colors.primaryAccent else colors.textPrimary,
                                    fontWeight = if (palette == currentPalette) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                onSelectPalette(palette)
                                showPaletteDropdown = false
                            }
                        )
                    }
                }
            }

            // Dark / Light Mode Toggle Switch
            IconButton(
                onClick = onToggleDarkMode,
                modifier = Modifier
                    .size(if (isCompact) 32.dp else 36.dp)
                    .testTag("dark_mode_toggle")
            ) {
                Icon(
                    imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                    tint = if (isDarkMode) colors.primaryAccent else colors.secondaryAccent,
                    modifier = Modifier.size(if (isCompact) 18.dp else 20.dp)
                )
            }

            // Generation Queue badge button
            Box(contentAlignment = Alignment.TopEnd) {
                IconButton(
                    onClick = onQueueClick,
                    modifier = Modifier
                        .size(if (isCompact) 32.dp else 36.dp)
                        .testTag("queue_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "Generation Queue",
                        tint = if (activeQueueCount > 0) colors.primaryAccent else colors.textSecondary,
                        modifier = Modifier.size(if (isCompact) 18.dp else 20.dp)
                    )
                }
                if (activeQueueCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(colors.primaryAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = activeQueueCount.toString(),
                            color = colors.background,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Settings button
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .size(if (isCompact) 32.dp else 36.dp)
                    .testTag("settings_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Studio Settings",
                    tint = colors.textSecondary,
                    modifier = Modifier.size(if (isCompact) 18.dp else 20.dp)
                )
            }
        }
    }
}

@Composable
fun SaturnBottomNav(
    currentScreen: String,
    onNavigate: (String) -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (isCompact) 8.dp else 16.dp, vertical = if (isCompact) 6.dp else 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .clip(CircleShape)
                .background(colors.surfaceCard.copy(alpha = 0.95f))
                .border(1.dp, colors.border, CircleShape)
                .padding(horizontal = if (isCompact) 6.dp else 8.dp, vertical = if (isCompact) 4.dp else 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavTabItem(
                label = "Home",
                icon = Icons.Default.Home,
                isSelected = currentScreen == "home",
                onClick = { onNavigate("home") },
                testTag = "nav_home",
                colors = colors,
                isCompact = isCompact
            )

            NavTabItem(
                label = "Cinema",
                icon = Icons.Default.Movie,
                isSelected = currentScreen == "cinema",
                onClick = { onNavigate("cinema") },
                testTag = "nav_cinema",
                colors = colors,
                isCompact = isCompact
            )

            // Primary Glowing Create Action Button
            Box(
                modifier = Modifier
                    .padding(horizontal = if (isCompact) 2.dp else 4.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(colors.primaryAccent, colors.secondaryAccent)
                        )
                    )
                    .clickable { onNavigate("create") }
                    .padding(horizontal = if (isCompact) 14.dp else 18.dp, vertical = if (isCompact) 8.dp else 10.dp)
                    .testTag("nav_create"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Create",
                        tint = if (colors.isDark) colors.background else Color.White,
                        modifier = Modifier.size(if (isCompact) 16.dp else 18.dp)
                    )
                    Spacer(modifier = Modifier.width(if (isCompact) 4.dp else 6.dp))
                    Text(
                        text = "Create",
                        color = if (colors.isDark) colors.background else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (isCompact) 11.sp else 13.sp
                    )
                }
            }

            NavTabItem(
                label = "Hub",
                icon = Icons.Default.Layers,
                isSelected = currentScreen == "hub",
                onClick = { onNavigate("hub") },
                testTag = "nav_hub",
                colors = colors,
                isCompact = isCompact
            )

            NavTabItem(
                label = "Projects",
                icon = Icons.Default.Folder,
                isSelected = currentScreen == "projects",
                onClick = { onNavigate("projects") },
                testTag = "nav_projects",
                colors = colors,
                isCompact = isCompact
            )
        }
    }
}

@Composable
private fun NavTabItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    colors: com.example.ui.theme.SaturnColors = SaturnTheme.colors,
    isCompact: Boolean = false
) {
    Column(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = if (isCompact) 6.dp else 10.dp, vertical = if (isCompact) 4.dp else 6.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) colors.primaryAccent else colors.textSecondary,
            modifier = Modifier.size(if (isCompact) 18.dp else 20.dp)
        )
        Text(
            text = label,
            color = if (isSelected) colors.primaryAccent else colors.textSecondary,
            fontSize = if (isCompact) 9.sp else 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun CostConfirmationDialog(
    providerName: String,
    modelName: String,
    resolution: String,
    duration: Int,
    estimatedCost: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = SaturnTheme.colors

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        containerColor = colors.surfaceCard,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = colors.primaryAccent)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Confirm Generation", color = colors.textPrimary, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "You are about to initiate creative generation with the following parameters:",
                    color = colors.textSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                CostRow("Provider", providerName, colors = colors)
                CostRow("Model", modelName, colors = colors)
                CostRow("Resolution", resolution, colors = colors)
                if (duration > 0) {
                    CostRow("Duration", "${duration}s", colors = colors)
                }
                CostRow("Estimated Cost", estimatedCost, highlight = true, colors = colors)
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                modifier = Modifier.testTag("confirm_generation_button")
            ) {
                Text("Generate Now", color = if (colors.isDark) colors.background else Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = colors.textSecondary)
            }
        }
    )
}

@Composable
private fun CostRow(
    label: String,
    value: String,
    highlight: Boolean = false,
    colors: com.example.ui.theme.SaturnColors
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = colors.textSecondary, fontSize = 12.sp)
        Text(
            value,
            color = if (highlight) colors.secondaryAccent else colors.textPrimary,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Medium,
            fontSize = 12.sp
        )
    }
}

@Composable
fun PromptEnhancerDialog(
    result: PromptEnhancer.EnhancedPromptResult,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = SaturnTheme.colors

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        containerColor = colors.surfaceCard,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = colors.primaryAccent)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Enhanced Prompt Preview", color = colors.textPrimary, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("ORIGINAL PROMPT", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(
                    result.original,
                    color = colors.textPrimary,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 12.dp)
                        .background(colors.surfaceCardLight, RoundedCornerShape(24.dp))
                        .padding(12.dp)
                )

                Text("CINEMATIC ENHANCED PROMPT", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(
                    result.enhanced,
                    color = colors.textPrimary,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 8.dp)
                        .background(colors.surfaceCardLight, RoundedCornerShape(24.dp))
                        .border(1.dp, colors.borderHighlight, RoundedCornerShape(24.dp))
                        .padding(12.dp)
                )

                Text(
                    result.explanation,
                    color = colors.secondaryAccent,
                    fontSize = 11.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onApply,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                modifier = Modifier.testTag("apply_enhanced_prompt_button")
            ) {
                Text("Apply to Studio", color = if (colors.isDark) colors.background else Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Keep Original", color = colors.textSecondary)
            }
        }
    )
}

@Composable
fun NewProjectDialog(
    onConfirm: (name: String, description: String) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = SaturnTheme.colors
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        containerColor = colors.surfaceCard,
        title = {
            Text("Create New Studio Project", color = colors.textPrimary, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    shape = RoundedCornerShape(24.dp),
                    label = { Text("Project Title") },
                    placeholder = { Text("e.g. Neo-Tokyo Commercial") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.primaryAccent,
                        unfocusedBorderColor = colors.border,
                        focusedTextColor = colors.textPrimary,
                        unfocusedTextColor = colors.textPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("project_name_input")
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    shape = RoundedCornerShape(24.dp),
                    label = { Text("Project Description") },
                    placeholder = { Text("e.g. 35mm film moodboard and teaser") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.primaryAccent,
                        unfocusedBorderColor = colors.border,
                        focusedTextColor = colors.textPrimary,
                        unfocusedTextColor = colors.textPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("project_desc_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(name.trim(), description.trim())
                    }
                },
                enabled = name.isNotBlank(),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                modifier = Modifier.testTag("create_project_confirm_button")
            ) {
                Text("Create Project", color = if (colors.isDark) colors.background else Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = colors.textSecondary)
            }
        }
    )
}
