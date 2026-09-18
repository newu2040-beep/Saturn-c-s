package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.AiCapability
import com.example.core.model.AiModel
import com.example.core.model.AiProvider
import com.example.core.prompt.PromptEnhancer
import com.example.ui.theme.SaturnBorder
import com.example.ui.theme.SaturnBorderHighlight
import com.example.ui.theme.SaturnCyan
import com.example.ui.theme.SaturnGold
import com.example.ui.theme.SaturnObsidian
import com.example.ui.theme.SaturnSurfaceCard
import com.example.ui.theme.SaturnTextPrimary
import com.example.ui.theme.SaturnTextSecondary
import com.example.ui.theme.SaturnViolet
import com.example.ui.viewmodel.SaturnViewModel

import androidx.compose.material.icons.filled.LockOpen
import com.example.core.model.AspectRatioOption
import com.example.core.model.MediaCategory
import com.example.ui.components.AspectRatioSelectorBar
import com.example.ui.theme.SaturnTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateScreen(
    viewModel: SaturnViewModel,
    onNavigate: (String) -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact

    val providers by viewModel.providers.collectAsState()
    val selectedProvider by viewModel.selectedProvider.collectAsState()
    val selectedModel by viewModel.selectedModel.collectAsState()
    val promptText by viewModel.promptText.collectAsState()
    val negativePromptText by viewModel.negativePromptText.collectAsState()
    val selectedAspectRatio by viewModel.selectedAspectRatio.collectAsState()
    val selectedResolution by viewModel.selectedResolution.collectAsState()
    val selectedDuration by viewModel.selectedDuration.collectAsState()
    val cameraMotion by viewModel.cameraMotion.collectAsState()
    val characters by viewModel.allCharacters.collectAsState()
    val elements by viewModel.allElements.collectAsState()

    var showNegativePrompt by remember { mutableStateOf(false) }
    var showProviderDropdown by remember { mutableStateOf(false) }
    var showModelDropdown by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = if (isCompact) 8.dp else 16.dp),
        verticalArrangement = Arrangement.spacedBy(if (isCompact) 8.dp else 14.dp),
        contentPadding = PaddingValues(top = if (isCompact) 4.dp else 8.dp, bottom = 100.dp)
    ) {
        // Zero Paywall Status Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(colors.primaryAccent.copy(alpha = 0.12f))
                    .border(1.dp, colors.primaryAccent.copy(alpha = 0.35f), CircleShape)
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LockOpen,
                            contentDescription = null,
                            tint = colors.primaryAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Zero Paywalls Active • 100% Unlocked",
                            color = colors.primaryAccent,
                            fontSize = if (isCompact) 11.sp else 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "BYOK / Free Tier",
                        color = colors.textSecondary,
                        fontSize = 10.sp
                    )
                }
            }
        }

        // Model & Provider Capability Bar
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.borderHighlight, RoundedCornerShape(28.dp))
            ) {
                Column(modifier = Modifier.padding(if (isCompact) 10.dp else 14.dp)) {
                    Text(
                        text = "ACTIVE AI MODEL & PROVIDER",
                        color = colors.textSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Provider selector button
                        Box(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                                    .background(colors.surfaceCardLight)
                                    .border(1.dp, colors.border, CircleShape)
                                    .clickable { showProviderDropdown = true }
                                    .padding(horizontal = 12.dp, vertical = if (isCompact) 6.dp else 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = selectedProvider.name,
                                    color = colors.textPrimary,
                                    fontSize = if (isCompact) 11.sp else 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1
                                )
                                Icon(Icons.Default.ExpandMore, contentDescription = null, tint = colors.textSecondary, modifier = Modifier.size(16.dp))
                            }

                            DropdownMenu(
                                expanded = showProviderDropdown,
                                onDismissRequest = { showProviderDropdown = false },
                                modifier = Modifier.background(colors.surfaceCard)
                            ) {
                                providers.forEach { provider ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                provider.name,
                                                color = if (provider.id == selectedProvider.id) colors.primaryAccent else colors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            viewModel.selectProvider(provider)
                                            showProviderDropdown = false
                                        }
                                    )
                                }
                            }
                        }

                        // Model selector button
                        Box(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                                    .background(colors.surfaceCardLight)
                                    .border(1.dp, colors.border, CircleShape)
                                    .clickable { showModelDropdown = true }
                                    .padding(horizontal = 12.dp, vertical = if (isCompact) 6.dp else 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = selectedModel.displayName.take(16),
                                    color = colors.secondaryAccent,
                                    fontSize = if (isCompact) 11.sp else 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1
                                )
                                Icon(Icons.Default.ExpandMore, contentDescription = null, tint = colors.secondaryAccent, modifier = Modifier.size(16.dp))
                            }

                            DropdownMenu(
                                expanded = showModelDropdown,
                                onDismissRequest = { showModelDropdown = false },
                                modifier = Modifier.background(colors.surfaceCard)
                            ) {
                                selectedProvider.models.forEach { model ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(
                                                    model.displayName,
                                                    color = if (model.id == selectedModel.id) colors.secondaryAccent else colors.textPrimary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    "${model.capability.label} • ${model.speedScore}",
                                                    color = colors.textSecondary,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        },
                                        onClick = {
                                            viewModel.selectModel(model)
                                            showModelDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    // Capabilities badges
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        CapabilityPill(selectedModel.capability.label, colors.primaryAccent)
                        CapabilityPill(selectedModel.qualityScore, colors.secondaryAccent)
                        CapabilityPill(selectedModel.speedScore, colors.textSecondary)
                    }
                }
            }
        }

        // Live Framing Canvas Preview
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF090B10)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val category = if (selectedModel.capability == AiCapability.VIDEO) MediaCategory.VIDEO else MediaCategory.PHOTO
                        viewModel.openCustomAspectRatio(category)
                    }
                    .border(1.dp, SaturnBorder, RoundedCornerShape(28.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val currentCategory = if (selectedModel.capability == AiCapability.VIDEO) MediaCategory.VIDEO else MediaCategory.PHOTO
                    val parsedRatio = AspectRatioOption.parseRatio(selectedAspectRatio, currentCategory)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .height(170.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFF131722))
                            .border(1.dp, Color(0x33F5A623), RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = when (selectedModel.capability) {
                                    AiCapability.VIDEO -> Icons.Default.Movie
                                    AiCapability.AUDIO, AiCapability.VOICE -> Icons.Default.GraphicEq
                                    else -> Icons.Default.Image
                                },
                                contentDescription = null,
                                tint = SaturnGold,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Viewport Frame [${selectedAspectRatio}]",
                                color = SaturnTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${parsedRatio.displayName} • ${selectedResolution}",
                                color = SaturnCyan,
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap to customize aspect ratio",
                                color = SaturnTextSecondary,
                                fontSize = 9.sp
                            )
                        }

                        // Framing guide corners
                        Box(modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .size(12.dp)
                            .border(1.dp, Color(0x55FFFFFF))
                        )
                        Box(modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .size(12.dp)
                            .border(1.dp, Color(0x55FFFFFF))
                        )
                    }
                }
            }
        }

        // Camera Motion Director Shortcut Bar
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = SaturnSurfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate("cinema") }
                    .border(1.dp, SaturnBorder, RoundedCornerShape(28.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Camera", tint = SaturnGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Cinema Camera Director", color = SaturnTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("${cameraMotion.motionType} • ${cameraMotion.lens} • ${cameraMotion.lighting}", color = SaturnTextSecondary, fontSize = 10.sp)
                        }
                    }
                    Text("Configure", color = SaturnCyan, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Prompt Input Area with Enhance Button
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("STUDIO PROMPT", color = SaturnTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    TextButton(
                        onClick = { viewModel.triggerPromptEnhancement() },
                        modifier = Modifier.testTag("enhance_prompt_button")
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SaturnViolet, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Enhance Prompt", color = SaturnViolet, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedTextField(
                    value = promptText,
                    onValueChange = { viewModel.promptText.value = it },
                    placeholder = { Text("Describe visual composition, subject, camera, lighting, motion...") },
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SaturnGold,
                        unfocusedBorderColor = SaturnBorder,
                        focusedTextColor = SaturnTextPrimary,
                        unfocusedTextColor = SaturnTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("prompt_input_field"),
                    minLines = 3
                )
            }
        }

        // Quick Tag Chips (Characters, Elements & Cinematic Styles)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("QUICK INSERTS", color = SaturnTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)

                // Characters
                if (characters.isNotEmpty()) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(characters) { char ->
                            QuickChip(
                                label = char.name,
                                icon = Icons.Default.Person,
                                color = SaturnCyan,
                                onClick = { viewModel.appendToPrompt(char.identityToken) }
                            )
                        }
                    }
                }

                // Styles
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(PromptEnhancer.styleChips) { style ->
                        QuickChip(
                            label = style,
                            color = SaturnGold,
                            onClick = { viewModel.appendToPrompt(style) }
                        )
                    }
                }
            }
        }

        // Negative Prompt Accordion
        item {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showNegativePrompt = !showNegativePrompt }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (showNegativePrompt) "Hide Negative Prompt" else "+ Add Negative Prompt",
                        color = SaturnTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                AnimatedVisibility(visible = showNegativePrompt) {
                    OutlinedTextField(
                        value = negativePromptText,
                        onValueChange = { viewModel.negativePromptText.value = it },
                        placeholder = { Text("What to avoid (blurry, watermarks, jitter)...") },
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SaturnBorderHighlight,
                            unfocusedBorderColor = SaturnBorder,
                            focusedTextColor = SaturnTextPrimary,
                            unfocusedTextColor = SaturnTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            }
        }

        // Dynamic Model Capability Controls: ONLY shown if model supports them!
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = SaturnSurfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SaturnBorder, RoundedCornerShape(28.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "CAPABILITY PARAMETERS",
                        color = SaturnTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Aspect Ratio with presets and custom ratio dialog trigger
                    val currentCategory = if (selectedModel.capability == AiCapability.VIDEO) MediaCategory.VIDEO else MediaCategory.PHOTO
                    AspectRatioSelectorBar(
                        currentRatio = selectedAspectRatio,
                        currentCategory = currentCategory,
                        onSelectRatio = { viewModel.selectedAspectRatio.value = it },
                        onOpenCustomDialog = { viewModel.openCustomAspectRatio(currentCategory) }
                    )

                    // Resolution (only if model has resolutions)
                    if (selectedModel.resolutions.isNotEmpty()) {
                        Column {
                            Text("Resolution", color = SaturnTextPrimary, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                selectedModel.resolutions.forEach { res ->
                                    SelectableChip(
                                        label = res,
                                        isSelected = res == selectedResolution,
                                        onClick = { viewModel.selectedResolution.value = res }
                                    )
                                }
                            }
                        }
                    }

                    // Duration (only if model supports video duration)
                    if (selectedModel.durations.isNotEmpty()) {
                        Column {
                            Text("Video Duration", color = SaturnTextPrimary, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                selectedModel.durations.forEach { dur ->
                                    SelectableChip(
                                        label = "${dur}s",
                                        isSelected = dur == selectedDuration,
                                        onClick = { viewModel.selectedDuration.value = dur }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Generate CTA with Zero Paywall Unlocked Execution
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { viewModel.generate() },
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (isCompact) 44.dp else 52.dp)
                        .testTag("generate_action_button")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = if (colors.isDark) colors.background else Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Generate Project Asset",
                                color = if (colors.isDark) colors.background else Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = if (isCompact) 13.sp else 15.sp
                            )
                        }

                        // Live Zero Paywall Tag
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0x33000000))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Zero Paywall • Unlocked",
                                color = if (colors.isDark) colors.background else Color.White,
                                fontSize = if (isCompact) 10.sp else 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CapabilityPill(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFF1D2230))
            .border(1.dp, color.copy(alpha = 0.4f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text, color = color, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun QuickChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFF1B202D))
            .border(1.dp, SaturnBorder, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(label, color = color, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun SelectableChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (isSelected) SaturnGold else Color(0xFF181D2A))
            .border(1.dp, if (isSelected) SaturnGold else SaturnBorder, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) SaturnObsidian else SaturnTextPrimary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
