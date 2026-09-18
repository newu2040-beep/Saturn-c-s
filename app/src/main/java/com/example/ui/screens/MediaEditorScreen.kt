package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.AspectRatioOption
import com.example.core.model.MediaCategory
import com.example.ui.components.AspectRatioSelectorBar
import com.example.ui.theme.SaturnTheme
import com.example.ui.viewmodel.SaturnViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun MediaEditorScreen(
    viewModel: SaturnViewModel,
    onBack: () -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact

    var activeTool by remember { mutableStateOf("crop") } // crop, grade, inpaint, upscale, background, relight
    var brightness by remember { mutableFloatStateOf(0f) } // -50 to 50
    var contrast by remember { mutableFloatStateOf(1f) } // 0.5 to 2.0
    var saturation by remember { mutableFloatStateOf(1.2f) } // 0.0 to 2.0
    var upscaleFactor by remember { mutableStateOf("4x") }
    var selectedBackground by remember { mutableStateOf("Studio Dark Stage") }
    var editorMediaCategory by remember { mutableStateOf(MediaCategory.PHOTO) }

    val selectedAspectRatio by viewModel.selectedAspectRatio.collectAsState()

    val tools = listOf(
        EditorToolItem("crop", "Aspect & Crop", Icons.Default.Crop),
        EditorToolItem("grade", "Color Grade", Icons.Default.Tune),
        EditorToolItem("inpaint", "Generative Fill", Icons.Default.Brush),
        EditorToolItem("upscale", "AI Upscaler", Icons.Default.HighQuality),
        EditorToolItem("relight", "Relighting", Icons.Default.Flare),
        EditorToolItem("background", "Background Swap", Icons.Default.Layers)
    )

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
                    Text("MEDIA EDITOR", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text("GPU Filter & Generative Studio", color = colors.textPrimary, fontSize = if (isCompact) 16.sp else 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Live Canvas GPU Viewport Preview
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.borderHighlight, RoundedCornerShape(28.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(if (isCompact) 8.dp else 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (isCompact) 160.dp else 200.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(colors.surfaceCardLight),
                        contentAlignment = Alignment.Center
                    ) {
                        // Real-time Canvas filter rendering preview
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val matrix = ColorMatrix().apply {
                                setToSaturation(saturation)
                            }
                            drawRect(
                                color = colors.border,
                                size = size,
                                colorFilter = ColorFilter.colorMatrix(matrix)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = colors.primaryAccent,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Live Grade: Brightness ${(brightness).toInt()} • Contrast ${"%.1f".format(contrast)} • Sat ${"%.1f".format(saturation)}",
                                color = colors.textPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                            if (activeTool == "upscale") {
                                Text("Upscaling Mode: $upscaleFactor AI Super Resolution", color = colors.secondaryAccent, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }

        // Tool Selector Tabs
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tools) { tool ->
                    ToolTab(
                        item = tool,
                        isSelected = tool.id == activeTool,
                        onClick = { activeTool = tool.id }
                    )
                }
            }
        }

        // Tool Controls Panel
        item {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colors.border, RoundedCornerShape(28.dp))
            ) {
                Column(modifier = Modifier.padding(if (isCompact) 10.dp else 16.dp)) {
                    when (activeTool) {
                        "crop" -> {
                            Text("ASPECT RATIO & COMPOSITION FRAMING", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))

                            // Category selector (Photo vs Video)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(CircleShape)
                                    .background(colors.surfaceCardLight)
                                    .padding(3.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(CircleShape)
                                        .background(if (editorMediaCategory == MediaCategory.PHOTO) colors.primaryAccent else Color.Transparent)
                                        .clickable { editorMediaCategory = MediaCategory.PHOTO }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Photo Ratios", color = if (editorMediaCategory == MediaCategory.PHOTO) (if (colors.isDark) colors.background else Color.White) else colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(CircleShape)
                                        .background(if (editorMediaCategory == MediaCategory.VIDEO) colors.primaryAccent else Color.Transparent)
                                        .clickable { editorMediaCategory = MediaCategory.VIDEO }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Video Ratios", color = if (editorMediaCategory == MediaCategory.VIDEO) (if (colors.isDark) colors.background else Color.White) else colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            AspectRatioSelectorBar(
                                currentRatio = selectedAspectRatio,
                                currentCategory = editorMediaCategory,
                                onSelectRatio = { viewModel.selectedAspectRatio.value = it },
                                onOpenCustomDialog = { viewModel.openCustomAspectRatio(editorMediaCategory) }
                            )
                        }
                        "grade" -> {
                            Text("COLOR GRADING SLIDERS", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(10.dp))

                            SliderRow("Exposure / Brightness", brightness, -50f..50f) { brightness = it }
                            SliderRow("Contrast Multiplier", contrast, 0.5f..2.0f) { contrast = it }
                            SliderRow("Saturation", saturation, 0.0f..2.0f) { saturation = it }
                        }
                        "upscale" -> {
                            Text("AI CLARITY & RESOLUTION EXPANSION", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Enhance micro-textures and remove compression artifacts with generative upscaling.", color = colors.textSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("2x Ultra HD", "4x Cinema Master", "8x Print Ready").forEach { opt ->
                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(if (upscaleFactor == opt) colors.primaryAccent else colors.surfaceCardLight)
                                            .clickable { upscaleFactor = opt }
                                            .padding(horizontal = 14.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = opt,
                                            color = if (upscaleFactor == opt) (if (colors.isDark) colors.background else Color.White) else colors.textPrimary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                        "background" -> {
                            Text("BACKGROUND GENERATIVE REPLACEMENT", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            listOf("Studio Dark Stage", "Cyberpunk Tokyo Balcony", "Golden Hour Desert", "Minimal Scandinavian Gallery").forEach { bg ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(CircleShape)
                                        .clickable { selectedBackground = bg }
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(if (selectedBackground == bg) colors.primaryAccent else colors.surfaceCardLight)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(bg, color = if (selectedBackground == bg) colors.primaryAccent else colors.textPrimary, fontSize = 13.sp)
                                }
                            }
                        }
                        else -> {
                            Text("GENERATIVE BRUSH & INPAINT", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Tap on canvas to paint mask area to replace, erase, or relight.", color = colors.textSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Apply Action Button
        item {
            Button(
                onClick = { onBack() },
                colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("apply_editor_changes_button")
            ) {
                Icon(Icons.Default.Done, contentDescription = null, tint = if (colors.isDark) colors.background else Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save & Export Edited Asset", color = if (colors.isDark) colors.background else Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

data class EditorToolItem(val id: String, val title: String, val icon: ImageVector)

@Composable
private fun ToolTab(
    item: EditorToolItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = SaturnTheme.colors
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (isSelected) colors.primaryAccent else colors.surfaceCard)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(item.icon, contentDescription = null, tint = if (isSelected) (if (colors.isDark) colors.background else Color.White) else colors.textSecondary, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(item.title, color = if (isSelected) (if (colors.isDark) colors.background else Color.White) else colors.textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun SliderRow(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    val colors = SaturnTheme.colors
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = colors.textPrimary, fontSize = 12.sp)
            Text("${"%.1f".format(value)}", color = colors.secondaryAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = colors.primaryAccent,
                activeTrackColor = colors.primaryAccent,
                inactiveTrackColor = colors.surfaceCardLight
            )
        )
    }
}
