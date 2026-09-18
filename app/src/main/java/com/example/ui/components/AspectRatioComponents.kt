package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.AspectRatioOption
import com.example.core.model.MediaCategory
import com.example.ui.theme.SaturnTheme
import kotlin.math.roundToInt

@Composable
fun AspectRatioSelectorBar(
    currentRatio: String,
    currentCategory: MediaCategory = MediaCategory.PHOTO,
    onSelectRatio: (String) -> Unit,
    onOpenCustomDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = SaturnTheme.colors
    val presets = if (currentCategory == MediaCategory.VIDEO) {
        AspectRatioOption.VIDEO_PRESETS
    } else {
        AspectRatioOption.PHOTO_PRESETS
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AspectRatio,
                    contentDescription = null,
                    tint = colors.primaryAccent,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Aspect Ratio [${currentRatio}]",
                    color = colors.textPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colors.surfaceCardLight)
                    .border(1.dp, colors.primaryAccent.copy(alpha = 0.5f), CircleShape)
                    .clickable { onOpenCustomDialog() }
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .testTag("open_custom_aspect_ratio_dialog"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Tune, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Custom...", color = colors.primaryAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(presets) { option ->
                val isSelected = option.ratioKey.equals(currentRatio, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isSelected) colors.primaryAccent.copy(alpha = 0.25f) else colors.surfaceCardLight)
                        .border(1.dp, if (isSelected) colors.primaryAccent else colors.border, CircleShape)
                        .clickable { onSelectRatio(option.ratioKey) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("ratio_chip_${option.ratioKey.replace(':', '_')}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Mini aspect ratio icon representation
                        MiniRatioIcon(
                            widthRatio = option.widthRatio,
                            heightRatio = option.heightRatio,
                            tint = if (isSelected) colors.primaryAccent else colors.textSecondary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = option.ratioKey,
                            color = if (isSelected) colors.primaryAccent else colors.textPrimary,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            item {
                val isCustomSelected = presets.none { it.ratioKey.equals(currentRatio, ignoreCase = true) }
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isCustomSelected) colors.secondaryAccent.copy(alpha = 0.25f) else colors.surfaceCardLight)
                        .border(1.dp, if (isCustomSelected) colors.secondaryAccent else colors.border, CircleShape)
                        .clickable { onOpenCustomDialog() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = colors.secondaryAccent, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isCustomSelected) "Custom: $currentRatio" else "Custom Ratio",
                            color = colors.secondaryAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MiniRatioIcon(
    widthRatio: Float,
    heightRatio: Float,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(12.dp)) {
        val maxDim = size.minDimension
        val ratio = (widthRatio / heightRatio).coerceIn(0.25f, 4f)
        val w: Float
        val h: Float
        if (ratio >= 1f) {
            w = maxDim
            h = maxDim / ratio
        } else {
            h = maxDim
            w = maxDim * ratio
        }
        val left = (size.width - w) / 2f
        val top = (size.height - h) / 2f

        drawRect(
            color = tint,
            topLeft = Offset(left, top),
            size = Size(w, h),
            style = Stroke(width = 1.5.dp.toPx())
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomAspectRatioDialog(
    initialRatio: String,
    initialCategory: MediaCategory = MediaCategory.PHOTO,
    onDismiss: () -> Unit,
    onApplyRatio: (String, Float, Float) -> Unit
) {
    val colors = SaturnTheme.colors
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    var tabIndex by remember { mutableIntStateOf(if (initialCategory == MediaCategory.VIDEO) 1 else 0) }

    val parsed = remember(initialRatio) {
        AspectRatioOption.parseRatio(initialRatio, initialCategory)
    }

    var customWidth by remember { mutableFloatStateOf(parsed.widthRatio) }
    var customHeight by remember { mutableFloatStateOf(parsed.heightRatio) }
    var customDecimalRatio by remember { mutableStateOf(String.format("%.2f", parsed.numericRatio)) }

    val currentCalculatedRatio = remember(customWidth, customHeight) {
        if (customHeight > 0f) customWidth / customHeight else 1f
    }

    val orientationLabel = remember(currentCalculatedRatio) {
        when {
            currentCalculatedRatio > 2.2f -> "Cinematic Ultrawide"
            currentCalculatedRatio > 1.2f -> "Landscape (Horizontal)"
            currentCalculatedRatio < 0.8f -> "Portrait (Vertical)"
            else -> "Square / Balanced"
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(28.dp),
        containerColor = colors.surfaceCard,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(colors.primaryAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AspectRatio, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("CUSTOM ASPECT RATIO", color = colors.primaryAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Text("Frame & Composition Director", color = colors.textPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = colors.textSecondary)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Category Tabs (Photos / Stills vs Videos / Motion vs Precision Builder)
                TabRow(
                    selectedTabIndex = tabIndex,
                    containerColor = colors.surfaceCardLight,
                    contentColor = colors.primaryAccent,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[tabIndex]),
                            color = colors.primaryAccent
                        )
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, colors.border, RoundedCornerShape(16.dp))
                ) {
                    Tab(
                        selected = tabIndex == 0,
                        onClick = {
                            tabIndex = 0
                            selectedCategory = MediaCategory.PHOTO
                        },
                        text = { Text("Photo Ratios", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        icon = { Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(14.dp)) }
                    )
                    Tab(
                        selected = tabIndex == 1,
                        onClick = {
                            tabIndex = 1
                            selectedCategory = MediaCategory.VIDEO
                        },
                        text = { Text("Cinema Video", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        icon = { Icon(Icons.Default.Movie, contentDescription = null, modifier = Modifier.size(14.dp)) }
                    )
                    Tab(
                        selected = tabIndex == 2,
                        onClick = {
                            tabIndex = 2
                        },
                        text = { Text("Custom Math", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        icon = { Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(14.dp)) }
                    )
                }

                // Visual Framing Canvas Box Preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF07090D))
                        .border(1.dp, colors.border, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Draw dynamic aspect ratio viewport frame with rule-of-thirds grid
                    Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                        val canvasW = size.width
                        val canvasH = size.height
                        val safeAspect = currentCalculatedRatio.coerceIn(0.2f, 4.0f)

                        val frameW: Float
                        val frameH: Float

                        if (canvasW / canvasH > safeAspect) {
                            frameH = canvasH
                            frameW = canvasH * safeAspect
                        } else {
                            frameW = canvasW
                            frameH = canvasW / safeAspect
                        }

                        val left = (canvasW - frameW) / 2f
                        val top = (canvasH - frameH) / 2f

                        // Background shaded area
                        drawRect(
                            color = Color(0x33000000),
                            topLeft = Offset(0f, 0f),
                            size = size
                        )

                        // Clear viewport framing
                        drawRect(
                            color = Color(0x22FFFFFF),
                            topLeft = Offset(left, top),
                            size = Size(frameW, frameH)
                        )

                        // Framing Border
                        drawRect(
                            color = colors.primaryAccent,
                            topLeft = Offset(left, top),
                            size = Size(frameW, frameH),
                            style = Stroke(width = 2.dp.toPx())
                        )

                        // Rule of Thirds grid lines (subtle dashed)
                        val oneThirdW = frameW / 3f
                        val oneThirdH = frameH / 3f
                        val dashEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)

                        // Vertical grid lines
                        drawLine(
                            color = Color(0x55FFFFFF),
                            start = Offset(left + oneThirdW, top),
                            end = Offset(left + oneThirdW, top + frameH),
                            strokeWidth = 1f,
                            pathEffect = dashEffect
                        )
                        drawLine(
                            color = Color(0x55FFFFFF),
                            start = Offset(left + 2 * oneThirdW, top),
                            end = Offset(left + 2 * oneThirdW, top + frameH),
                            strokeWidth = 1f,
                            pathEffect = dashEffect
                        )

                        // Horizontal grid lines
                        drawLine(
                            color = Color(0x55FFFFFF),
                            start = Offset(left, top + oneThirdH),
                            end = Offset(left + frameW, top + oneThirdH),
                            strokeWidth = 1f,
                            pathEffect = dashEffect
                        )
                        drawLine(
                            color = Color(0x55FFFFFF),
                            start = Offset(left, top + 2 * oneThirdH),
                            end = Offset(left + frameW, top + 2 * oneThirdH),
                            strokeWidth = 1f,
                            pathEffect = dashEffect
                        )
                    }

                    // Floating Live Dimension Tag
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xCC000000))
                            .border(1.dp, colors.primaryAccent.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${customWidth.roundToInt()}:${customHeight.roundToInt()} • ${String.format("%.2f", currentCalculatedRatio)}:1",
                            color = colors.primaryAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = orientationLabel,
                            color = colors.secondaryAccent,
                            fontSize = 9.sp
                        )
                    }
                }

                // Presets or Custom Sliders based on Tab
                when (tabIndex) {
                    0 -> {
                        // Photo Presets
                        Text("Standard Photography Presets", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AspectRatioOption.PHOTO_PRESETS.forEach { opt ->
                                val isSel = (opt.widthRatio == customWidth && opt.heightRatio == customHeight) ||
                                        (Math.abs(opt.numericRatio - currentCalculatedRatio) < 0.01f)
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(if (isSel) colors.primaryAccent.copy(alpha = 0.25f) else colors.surfaceCardLight)
                                        .border(1.dp, if (isSel) colors.primaryAccent else colors.border, CircleShape)
                                        .clickable {
                                            customWidth = opt.widthRatio
                                            customHeight = opt.heightRatio
                                            customDecimalRatio = String.format("%.2f", opt.numericRatio)
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(opt.displayName, color = if (isSel) colors.primaryAccent else colors.textPrimary, fontSize = 10.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal)
                                }
                            }
                        }
                    }
                    1 -> {
                        // Video Presets
                        Text("Cinema & Broadcast Video Presets", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AspectRatioOption.VIDEO_PRESETS.forEach { opt ->
                                val isSel = (opt.widthRatio == customWidth && opt.heightRatio == customHeight) ||
                                        (Math.abs(opt.numericRatio - currentCalculatedRatio) < 0.01f)
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(if (isSel) colors.primaryAccent.copy(alpha = 0.25f) else colors.surfaceCardLight)
                                        .border(1.dp, if (isSel) colors.primaryAccent else colors.border, CircleShape)
                                        .clickable {
                                            customWidth = opt.widthRatio
                                            customHeight = opt.heightRatio
                                            customDecimalRatio = String.format("%.2f", opt.numericRatio)
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(opt.displayName, color = if (isSel) colors.primaryAccent else colors.textPrimary, fontSize = 10.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal)
                                }
                            }
                        }
                    }
                    2 -> {
                        // Custom Numerical Builders
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Width Slider
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Width Unit: ${customWidth.roundToInt()}", color = colors.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Button(
                                        onClick = {
                                            val tmp = customWidth
                                            customWidth = customHeight
                                            customHeight = tmp
                                        },
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceCardLight),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Icon(Icons.Default.ScreenRotation, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Swap W ↔ H", color = colors.primaryAccent, fontSize = 10.sp)
                                    }
                                }
                            }
                            Slider(
                                value = customWidth,
                                onValueChange = { customWidth = it },
                                valueRange = 1f..32f,
                                steps = 30,
                                colors = SliderDefaults.colors(thumbColor = colors.primaryAccent, activeTrackColor = colors.primaryAccent)
                            )

                            // Height Slider
                            Text("Height Unit: ${customHeight.roundToInt()}", color = colors.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Slider(
                                value = customHeight,
                                onValueChange = { customHeight = it },
                                valueRange = 1f..32f,
                                steps = 30,
                                colors = SliderDefaults.colors(thumbColor = colors.secondaryAccent, activeTrackColor = colors.secondaryAccent)
                            )
                        }
                    }
                }

                // Rotate / Invert Quick Action & Estimated Output Dimensions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(colors.surfaceCardLight)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("TARGET ESTIMATE", color = colors.primaryAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        val wPx = if (currentCalculatedRatio >= 1f) 1920 else (1080 * currentCalculatedRatio).roundToInt()
                        val hPx = if (currentCalculatedRatio >= 1f) (1920 / currentCalculatedRatio).roundToInt() else 1920
                        Text("${wPx} × ${hPx} px (FHD Master)", color = colors.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            val temp = customWidth
                            customWidth = customHeight
                            customHeight = temp
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.primaryAccent.copy(alpha = 0.4f)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Rotate 90°", color = colors.primaryAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalKey = "${customWidth.roundToInt()}:${customHeight.roundToInt()}"
                    onApplyRatio(finalKey, customWidth, customHeight)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                shape = CircleShape,
                modifier = Modifier.testTag("apply_custom_aspect_ratio_button")
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Apply Aspect Ratio [${customWidth.roundToInt()}:${customHeight.roundToInt()}]", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 12.sp)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceCardLight),
                shape = CircleShape
            ) {
                Text("Cancel", color = colors.textSecondary, fontSize = 12.sp)
            }
        }
    )
}
