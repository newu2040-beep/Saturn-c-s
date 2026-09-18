package com.example.ui.components

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.core.model.AiCapability
import com.example.core.model.ExportMasterSettings
import com.example.core.model.GenerationJob
import com.example.ui.theme.SaturnTheme
import kotlinx.coroutines.delay

@Composable
fun InbuiltVideoPlayer(
    modifier: Modifier = Modifier,
    videoDurationSeconds: Int = 5,
    title: String = "Cinematic Render",
    previewUri: String? = null
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact

    var isPlaying by remember { mutableStateOf(true) }
    var currentProgress by remember { mutableFloatStateOf(0.4f) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var isMuted by remember { mutableStateOf(false) }
    var volume by remember { mutableFloatStateOf(0.85f) }
    var isLooping by remember { mutableStateOf(true) }

    LaunchedEffect(isPlaying, playbackSpeed) {
        while (isPlaying) {
            delay(100L)
            currentProgress += (0.1f / (videoDurationSeconds.toFloat() * 10f)) * playbackSpeed
            if (currentProgress >= 1f) {
                if (isLooping) {
                    currentProgress = 0f
                } else {
                    currentProgress = 1f
                    isPlaying = false
                }
            }
        }
    }

    val totalDurationSeconds = videoDurationSeconds
    val currentSeconds = (currentProgress * totalDurationSeconds).toInt()
    val timeDisplay = String.format("00:%02d / 00:%02d", currentSeconds, totalDurationSeconds)

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, colors.borderHighlight, RoundedCornerShape(28.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (isCompact) 10.dp else 14.dp)
        ) {
            // Player Viewport
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF07090E))
                    .border(1.dp, colors.border, RoundedCornerShape(24.dp))
                    .clickable { isPlaying = !isPlaying },
                contentAlignment = Alignment.Center
            ) {
                // Background visual / Simulated Film Render Canvas
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFD4AF37).copy(alpha = 0.35f),
                                Color(0xFF6366F1).copy(alpha = 0.15f),
                                Color.Transparent
                            ),
                            center = Offset(w * (0.3f + currentProgress * 0.4f), h * 0.45f),
                            radius = w * 0.6f
                        )
                    )

                    // Cinematic letterbox guide
                    drawRect(
                        color = Color.Black.copy(alpha = 0.3f),
                        topLeft = Offset.Zero,
                        size = Size(w, h * 0.08f)
                    )
                    drawRect(
                        color = Color.Black.copy(alpha = 0.3f),
                        topLeft = Offset(0f, h * 0.92f),
                        size = Size(w, h * 0.08f)
                    )
                }

                // Play / Pause central indicator overlay
                Box(
                    modifier = Modifier
                        .size(if (isCompact) 48.dp else 56.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.65f))
                        .border(1.dp, colors.primaryAccent.copy(alpha = 0.7f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = colors.primaryAccent,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Codec & Quality Pill
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .border(1.dp, colors.primaryAccent.copy(alpha = 0.5f), CircleShape)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "4K Master • 60 FPS • ProRes 422",
                        color = colors.primaryAccent,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Duration Pill
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(timeDisplay, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Scrubber Slider
            Slider(
                value = currentProgress,
                onValueChange = {
                    currentProgress = it
                },
                colors = SliderDefaults.colors(
                    thumbColor = colors.primaryAccent,
                    activeTrackColor = colors.primaryAccent,
                    inactiveTrackColor = colors.surfaceCardLight
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
                    .testTag("video_player_scrubber")
            )

            // Player Control Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Play / Pause & Skip
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { currentProgress = (currentProgress - 0.2f).coerceAtLeast(0f) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.FastRewind, contentDescription = "Rewind", tint = colors.textSecondary, modifier = Modifier.size(18.dp))
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(colors.primaryAccent)
                            .clickable { isPlaying = !isPlaying },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = if (colors.isDark) colors.background else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = { currentProgress = (currentProgress + 0.2f).coerceAtMost(1f) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.FastForward, contentDescription = "Forward", tint = colors.textSecondary, modifier = Modifier.size(18.dp))
                    }
                }

                // Speed Selector Pill
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colors.surfaceCardLight)
                        .border(1.dp, colors.border, CircleShape)
                        .clickable {
                            playbackSpeed = when (playbackSpeed) {
                                0.5f -> 1.0f
                                1.0f -> 1.5f
                                1.5f -> 2.0f
                                else -> 0.5f
                            }
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Speed, contentDescription = null, tint = colors.secondaryAccent, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${playbackSpeed}x", color = colors.textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Loop Toggle Pill
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (isLooping) colors.primaryAccent.copy(alpha = 0.2f) else colors.surfaceCardLight)
                        .border(1.dp, if (isLooping) colors.primaryAccent else colors.border, CircleShape)
                        .clickable { isLooping = !isLooping }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Repeat, contentDescription = null, tint = if (isLooping) colors.primaryAccent else colors.textSecondary, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isLooping) "Loop On" else "Loop Off", color = if (isLooping) colors.primaryAccent else colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Audio Mute Toggle
                IconButton(
                    onClick = { isMuted = !isMuted },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                        contentDescription = "Mute",
                        tint = if (isMuted) colors.textSecondary else colors.primaryAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun InbuiltAudioPlayer(
    modifier: Modifier = Modifier,
    title: String = "Spatial Soundtrack Master",
    artist: String = "Suno / ElevenLabs Neural Synth",
    durationSeconds: Int = 180
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact

    var isPlaying by remember { mutableStateOf(false) }
    var currentProgress by remember { mutableFloatStateOf(0.25f) }
    var volume by remember { mutableFloatStateOf(0.9f) }

    val infiniteTransition = rememberInfiniteTransition(label = "audio_anim")
    val pulseFactor by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(100L)
            currentProgress += (0.1f / durationSeconds.toFloat())
            if (currentProgress >= 1f) {
                currentProgress = 0f
                isPlaying = false
            }
        }
    }

    val currentSeconds = (currentProgress * durationSeconds).toInt()
    val currentFormatted = String.format("%02d:%02d", currentSeconds / 60, currentSeconds % 60)
    val totalFormatted = String.format("%02d:%02d", durationSeconds / 60, durationSeconds % 60)

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceCard),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, colors.borderHighlight, RoundedCornerShape(28.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (isCompact) 12.dp else 16.dp)
        ) {
            // Track Info Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(if (isCompact) 36.dp else 42.dp)
                            .clip(CircleShape)
                            .background(colors.primaryAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.GraphicEq, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(title, color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(artist, color = colors.textSecondary, fontSize = 11.sp)
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(colors.surfaceCardLight)
                        .border(1.dp, colors.border, CircleShape)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("24-bit • 96kHz FLAC", color = colors.secondaryAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Multi-bar Animated Waveform Visualizer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isCompact) 56.dp else 70.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF090B10))
                    .border(1.dp, colors.border, RoundedCornerShape(24.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val barCount = 32
                    val barWidth = size.width / (barCount * 1.5f)
                    val spacing = barWidth * 0.5f
                    val baseHeights = listOf(
                        0.4f, 0.6f, 0.9f, 0.5f, 0.8f, 1.0f, 0.7f, 0.4f,
                        0.3f, 0.7f, 0.85f, 0.6f, 0.95f, 0.5f, 0.8f, 0.4f,
                        0.6f, 0.9f, 0.7f, 0.8f, 0.45f, 0.65f, 0.85f, 0.9f,
                        0.5f, 0.7f, 0.6f, 0.4f, 0.8f, 0.5f, 0.3f, 0.6f
                    )

                    for (i in 0 until barCount) {
                        val x = i * (barWidth + spacing) + spacing
                        val ratio = i.toFloat() / barCount.toFloat()
                        val isPastProgress = ratio <= currentProgress

                        val heightMultiplier = if (isPlaying) {
                            baseHeights[i % baseHeights.size] * (0.6f + pulseFactor * 0.4f)
                        } else {
                            baseHeights[i % baseHeights.size] * 0.5f
                        }

                        val barH = (size.height * 0.85f * heightMultiplier).coerceAtLeast(4f)
                        val topY = (size.height - barH) / 2f

                        drawRoundRect(
                            color = if (isPastProgress) colors.primaryAccent else colors.primaryAccent.copy(alpha = 0.25f),
                            topLeft = Offset(x, topY),
                            size = Size(barWidth, barH),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Time and Progress Scrubber
            Slider(
                value = currentProgress,
                onValueChange = { currentProgress = it },
                colors = SliderDefaults.colors(
                    thumbColor = colors.primaryAccent,
                    activeTrackColor = colors.primaryAccent,
                    inactiveTrackColor = colors.surfaceCardLight
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .testTag("audio_player_scrubber")
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(currentFormatted, color = colors.primaryAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(totalFormatted, color = colors.textSecondary, fontSize = 10.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Player Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { currentProgress = (currentProgress - 0.1f).coerceAtLeast(0f) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.FastRewind, contentDescription = "Back 10s", tint = colors.textSecondary, modifier = Modifier.size(18.dp))
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(colors.primaryAccent)
                        .clickable { isPlaying = !isPlaying }
                        .testTag("audio_play_toggle_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = if (colors.isDark) colors.background else Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(
                    onClick = { currentProgress = (currentProgress + 0.1f).coerceAtMost(1f) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.FastForward, contentDescription = "Forward 10s", tint = colors.textSecondary, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
fun GalleryPreviewDialog(
    job: GenerationJob,
    exportSettings: ExportMasterSettings,
    onDismiss: () -> Unit,
    onExportHighQuality: (ExportMasterSettings) -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var showExportSheet by remember { mutableStateOf(false) }
    var activeCodec by remember { mutableStateOf(exportSettings.videoCodec) }
    var activeResolution by remember { mutableStateOf(exportSettings.videoResolution) }
    var isExporting by remember { mutableStateOf(false) }
    var exportSuccess by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colors.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(if (isCompact) 10.dp else 16.dp)
            ) {
                // Top Action Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(colors.surfaceCard)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = colors.textPrimary)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(colors.primaryAccent.copy(alpha = 0.15f))
                                .border(1.dp, colors.primaryAccent.copy(alpha = 0.5f), CircleShape)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = job.capability.label.uppercase(),
                                color = colors.primaryAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Button(
                        onClick = { showExportSheet = true },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("gallery_high_quality_export_button")
                    ) {
                        Icon(Icons.Default.HighQuality, contentDescription = null, tint = if (colors.isDark) colors.background else Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("HQ Export", color = if (colors.isDark) colors.background else Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Media Preview Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color(0xFF06070B))
                        .border(1.dp, colors.border, RoundedCornerShape(28.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    when (job.capability) {
                        AiCapability.VIDEO -> {
                            InbuiltVideoPlayer(
                                modifier = Modifier.fillMaxSize(),
                                title = job.prompt.take(30),
                                previewUri = job.localFileUris.firstOrNull() ?: job.outputUrls.firstOrNull()
                            )
                        }
                        AiCapability.AUDIO, AiCapability.VOICE -> {
                            InbuiltAudioPlayer(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                title = job.prompt.take(40),
                                artist = "${job.providerId.uppercase()} Neural Audio Engine"
                            )
                        }
                        else -> {
                            // High Res Image Viewport with interactive pinch zoom
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .pointerInput(Unit) {
                                        detectTransformGestures { _, pan, zoom, _ ->
                                            scale = (scale * zoom).coerceIn(1f, 4f)
                                            offset = if (scale > 1f) {
                                                Offset(offset.x + pan.x, offset.y + pan.y)
                                            } else {
                                                Offset.Zero
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer(
                                            scaleX = scale,
                                            scaleY = scale,
                                            translationX = offset.x,
                                            translationY = offset.y
                                        )
                                ) {
                                    val w = size.width
                                    val h = size.height
                                    drawCircle(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                Color(0xFFD4AF37).copy(alpha = 0.5f),
                                                Color(0xFF6366F1).copy(alpha = 0.3f),
                                                Color.Transparent
                                            ),
                                            center = Offset(w * 0.5f, h * 0.5f),
                                            radius = w * 0.7f
                                        )
                                    )
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Icon(Icons.Default.Image, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(48.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Pinch or Drag to Zoom & Inspect Master Asset", color = colors.textSecondary, fontSize = 11.sp)
                                    if (scale > 1f) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("${String.format("%.1f", scale)}x Zoom", color = colors.primaryAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Metadata Details Card
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
                            Text("PROMPT & MASTER METADATA", color = colors.primaryAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Text("Job #${job.id.takeLast(6)}", color = colors.textSecondary, fontSize = 10.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(job.prompt, color = colors.textPrimary, fontSize = 12.sp, maxLines = 3)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(colors.surfaceCardLight)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Model: ${job.modelId}", color = colors.textSecondary, fontSize = 10.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(colors.surfaceCardLight)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text("Bitrate: 100 Mbps", color = colors.secondaryAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    // High Quality Export Configuration Bottom Sheet / Dialog
    if (showExportSheet) {
        Dialog(
            onDismissRequest = { showExportSheet = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(28.dp),
                color = colors.surfaceCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.primaryAccent.copy(alpha = 0.6f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.HighQuality, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("High Quality Master Export", color = colors.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        IconButton(onClick = { showExportSheet = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = colors.textSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Select Cinema Codec", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    val codecs = listOf("Apple ProRes 422 HQ", "H.265 / HEVC 10-bit", "AV1 Film Master", "Lossless PNG Sequence")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(codecs) { codec ->
                            val isSel = codec == activeCodec
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (isSel) colors.primaryAccent.copy(alpha = 0.2f) else colors.surfaceCardLight)
                                    .border(1.dp, if (isSel) colors.primaryAccent else colors.border, CircleShape)
                                    .clickable { activeCodec = codec }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(codec, color = if (isSel) colors.primaryAccent else colors.textPrimary, fontSize = 11.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Master Resolution Preset", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    val resolutions = listOf("4K Cinema (3840x2160)", "1440p QHD", "1080p Master")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(resolutions) { res ->
                            val isSel = res == activeResolution
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (isSel) colors.primaryAccent.copy(alpha = 0.2f) else colors.surfaceCardLight)
                                    .border(1.dp, if (isSel) colors.primaryAccent else colors.border, CircleShape)
                                    .clickable { activeResolution = res }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(res, color = if (isSel) colors.primaryAccent else colors.textPrimary, fontSize = 11.sp, fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (exportSuccess) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(28.dp))
                                .background(Color(0xFF10B981).copy(alpha = 0.2f))
                                .border(1.dp, Color(0xFF10B981), RoundedCornerShape(28.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Export Render Complete! Saved to Gallery.", color = Color(0xFF10B981), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Button(
                        onClick = {
                            isExporting = true
                            val updated = exportSettings.copy(
                                videoCodec = activeCodec,
                                videoResolution = activeResolution
                            )
                            onExportHighQuality(updated)
                            isExporting = false
                            exportSuccess = true
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("confirm_export_master_button")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = if (colors.isDark) colors.background else Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isExporting) "Rendering Master..." else "Export Master File",
                            color = if (colors.isDark) colors.background else Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionsManagerDialog(
    onDismiss: () -> Unit,
    onAllGranted: () -> Unit
) {
    val colors = SaturnTheme.colors
    val isCompact = SaturnTheme.isCompact

    var notificationsGranted by remember { mutableStateOf(false) }
    var galleryGranted by remember { mutableStateOf(false) }
    var audioGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        notificationsGranted = results[Manifest.permission.POST_NOTIFICATIONS] ?: true
        galleryGranted = (results[Manifest.permission.READ_MEDIA_IMAGES] == true || results[Manifest.permission.READ_EXTERNAL_STORAGE] == true)
        audioGranted = results[Manifest.permission.RECORD_AUDIO] == true

        if (galleryGranted || notificationsGranted || audioGranted) {
            onAllGranted()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(28.dp),
            color = colors.surfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, colors.primaryAccent.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
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
                                .background(colors.primaryAccent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = colors.primaryAccent, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("PERMISSIONS", color = colors.primaryAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                            Text("Full Studio Access", color = colors.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = colors.textSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Grant permissions to enable instant notifications on generation completion, full-fidelity gallery export, and microphone voice cloning.",
                    color = colors.textSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Permission item 1: Notifications
                PermissionItemRow(
                    icon = Icons.Default.Notifications,
                    title = "Push Notifications",
                    description = "Alerts when multi-minute video & audio render jobs complete",
                    isGranted = notificationsGranted
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Permission item 2: Gallery / Files Access
                PermissionItemRow(
                    icon = Icons.Default.PhotoLibrary,
                    title = "Gallery & High-Res Storage",
                    description = "Save uncompressed 4K ProRes renders & load reference imagery",
                    isGranted = galleryGranted
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Permission item 3: Audio / Microphone
                PermissionItemRow(
                    icon = Icons.Default.Mic,
                    title = "Microphone & Voice Input",
                    description = "Live voice cloning and audio studio prompt dictation",
                    isGranted = audioGranted
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val perms = mutableListOf<String>()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            perms.add(Manifest.permission.POST_NOTIFICATIONS)
                            perms.add(Manifest.permission.READ_MEDIA_IMAGES)
                            perms.add(Manifest.permission.READ_MEDIA_VIDEO)
                            perms.add(Manifest.permission.READ_MEDIA_AUDIO)
                        } else {
                            perms.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                        perms.add(Manifest.permission.RECORD_AUDIO)
                        permissionLauncher.launch(perms.toTypedArray())
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primaryAccent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("grant_all_permissions_button")
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = if (colors.isDark) colors.background else Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Grant All Permissions", color = if (colors.isDark) colors.background else Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PermissionItemRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    isGranted: Boolean
) {
    val colors = SaturnTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(colors.surfaceCardLight)
            .border(1.dp, colors.border, RoundedCornerShape(24.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (isGranted) Color(0xFF10B981).copy(alpha = 0.2f) else colors.primaryAccent.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = if (isGranted) Color(0xFF10B981) else colors.primaryAccent, modifier = Modifier.size(18.dp))
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(description, color = colors.textSecondary, fontSize = 10.sp)
        }

        Spacer(modifier = Modifier.width(6.dp))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(if (isGranted) Color(0xFF10B981).copy(alpha = 0.2f) else colors.surfaceCard)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (isGranted) "Granted" else "Ready",
                color = if (isGranted) Color(0xFF10B981) else colors.textSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
